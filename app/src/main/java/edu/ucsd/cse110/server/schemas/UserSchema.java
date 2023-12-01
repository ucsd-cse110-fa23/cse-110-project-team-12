package edu.ucsd.cse110.server.schemas;

public class UserSchema {
    // Must be mongodb objectId format: 24 characters.
    public String _id;

    public String username;
    public String password;

    public UserSchema() {}
}
