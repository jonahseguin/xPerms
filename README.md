# xPerms

MongoDB backed permissions plugin for Bukkit

Designed for use across multiple servers // on networks

Features include
- Per-groups permissions with subgroups/inheritance
- Per-server groups 
- Accessible and easy to use yet powerful commands
- MongoDB backed
- Feature-rich commands, see /xperms

# Easy to use API

Example usage: 

      `
      XProfile profile = XPerms.getInstance().getCache().getProfile("Shawckz");

        Group groups = XPerms.getInstance().getGroupManager().getGroup(XPerms.getInstance().getPermServer(), "SomeGroup");
        if(groups == null) {
            groups =  new Group(XPerms.getInstance(),
                    XPerms.getInstance().getPermServer(),
                    "SomeGroup", //Group name
                    "&e[Prefix]", //Group prefix
                    "&9[Suffix]", //Group suffix
                    new Permissions(), //Or pass in a hashmap of permissions
                    new SubGroups(XPerms.getInstance()) //Or copy subgroups from an existing groups...
            );

            XPerms.getInstance().getGroupManager().registerGroup(XPerms.getInstance().getPermServer(), groups);
        }

        profile.getGroup().saveGroup(XPerms.getInstance().getPermServer(), groups); //Add the groups to the player's local groups

        profile.refreshPermissions();//Load permissions from groups
        `


# Coming soon:
- Optional Redis support for increased cross-server updates and functionality
- SQL & SQLite Support
- Configurable language file
