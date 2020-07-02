package com.precisionmedcare.jkkjwebsite.providers;

import java.util.Map;

public class BackStageApiProvider {
    public String getRoleByUserName(Map<String, Object> map) {
        String username = map.get("username").toString();
        StringBuilder sql = new StringBuilder();
        sql.append("select role.rolename\n" +
                "from nmn_user user,\n" +
                "     nmn_role role,\n" +
                "     nmn_user_role userrole\n" +
                "where user.status = 0\n" +
                "  and role.status = 0\n" +
                "  and user.id = userrole.userid\n" +
                "  and role.id = userrole.roleid\n" +
                "  and user.email = '").append(username).append("'");
        return sql.toString();
    }
}
