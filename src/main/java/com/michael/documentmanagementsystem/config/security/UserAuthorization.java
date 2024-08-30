package com.michael.documentmanagementsystem.config.security;

import com.michael.documentmanagementsystem.model.User;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class UserAuthorization implements AuthorizationManager<RequestAuthorizationContext> {

    private static final UriTemplate URI_TEMPLATE = new UriTemplate("/workspaces/{nid}");

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {

        Map<String, String> uriVariables = URI_TEMPLATE.match(context.getRequest().getRequestURI());
        Long nid = Long.parseLong(uriVariables.get("nid"));
        Authentication authentication = authenticationSupplier.get();
        return new AuthorizationDecision(hasUserNid(authentication, nid));
    }

    public boolean hasUserNid(Authentication authentication, Long nid) {
        Long jwtNid = ((User) (authentication.getPrincipal())).getNID();
        return jwtNid.equals(nid);
    }

}
