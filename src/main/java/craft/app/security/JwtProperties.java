package craft.app.security;

//https://www.youtube.com/watch?v=oVpFJr-Z35A&list=PLVApX3evDwJ1d0lKKHssPQvzv2Ao3e__Q&index=29
public class JwtProperties {
    public static final String SECRET = "HELLOWorld1986";
    public static final int EXPIRATION_TIME = 864000000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
