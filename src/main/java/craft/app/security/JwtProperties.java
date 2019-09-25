package craft.app.security;

/**
 * This class defines the constant properties which will be used when created the JWT, to keep them
 * consistent between usage points.
 *
 * See https://www.youtube.com/watch?v=oVpFJr-Z35A&list=PLVApX3evDwJ1d0lKKHssPQvzv2Ao3e__Q&index=29
 * for details
 */

public class JwtProperties {
    
    /**
     * This is the text with which the token will be hashed - it's an arbitrary string of text
     */
    public static final String SECRET = "HELLOWorld1986";
    
    /**
     * The length of time for which the JWT will be valid - set here to 10 days which is not best
     * practice but prioritised other things over learning how to refresh a token
     */
    public static final int EXPIRATION_TIME = 1000*60*60*24*10;
    
    /**
     * Declaring the prefix which will be included in the JWT, needs to be "Bearer "
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    
    /**
     * Declares which header field the JWT will be sent in - needs to be set to Authorization
     * (with that spelling) as that's what the authorisation service will look for
     */
    public static final String HEADER_STRING = "Authorization";
}
