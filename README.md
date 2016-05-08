# xPerms

MongoDB backed permissions plugin for Bukkit

Designed for use across multiple servers // on networks

Features include
- Per-group permissions with subgroups/inheritance
- Per-server groups 
- Accessible and easy to use yet powerful commands
- MongoDB backed
- Easy to use API
- 

Example usage: 

      XProfile profile = XPerms.getInstance().getCache().getProfile("Shawckz");

        Group group = XPerms.getInstance().getGroupManager().getGroup(XPerms.getInstance().getPermServer(), "SomeGroup");
        if(group == null) {
            group =  new Group(XPerms.getInstance(),
                    XPerms.getInstance().getPermServer(),
                    "SomeGroup", //Group name
                    "&e[Prefix]", //Group prefix
                    "&9[Suffix]", //Group suffix
                    new Permissions(), //Or pass in a hashmap of permissions
                    new SubGroups(XPerms.getInstance()) //Or copy subgroups from an existing group...
            );

            XPerms.getInstance().getGroupManager().registerGroup(XPerms.getInstance().getPermServer(), group);
        }

        profile.getGroup().saveGroup(XPerms.getInstance().getPermServer(), group); //Add the group to the player's local groups

        profile.refreshPermissions();//Load permissions from group

