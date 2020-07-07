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
    public String queryAllUser(Map<String, Object> map){
        String keyword = map.get("keyword").toString();
        StringBuilder sql = new StringBuilder();
        sql.append("select user.id userid,\n" +
                "       user.email,\n" +
                "       user.nickname,\n" +
                "       user_details.email detailsEmail,\n" +
                "       user_details.address,\n" +
                "       user_details.idcard,\n" +
                "       user_details.phone,\n" +
                "       user_details.name,\n" +
                "       user_details.id userdetailsid\n" +
                "from nmn_user user\n" +
                "         left join nmn_user_details user_details on user.id = user_details.user_id\n" +
                "where user.status = 0\n" +
                "  and user.email <> 'admin'");
        if(!"".equals(keyword)){
            sql.
                    append(" and (user.email like '%").
                    append(keyword).
                    append("%' or user.nickname like '%").
                    append(keyword).
                    append("%' or user_details.email like '%").
                    append(keyword).
                    append("%' or user_details.phone like '%").
                    append(keyword).
                    append("%')");
        }
        return sql.toString();
    }
    public String queryNmn(Map<String, Object> map){
        String keyword = map.get("keyword").toString();
        StringBuilder sql = new StringBuilder();
        sql.append("select nmn.id,\n" +
                "       nmn.status,\n" +
                "       nmn.cover_img   coverImg,\n" +
                "       nmn.create_time createTime,\n" +
                "       nmn.online,\n" +
                "       nmn.price,\n" +
                "       nmn.score,\n" +
                "       nmn.summary,\n" +
                "       nmn.title,\n" +
                "       nmn.view_num    viewNum\n" +
                "from nmn_nmn nmn\n" +
                "where status = 0");
        if(!"".equals(keyword)){
            sql.
                    append(" and (nmn.title like '%)").
                    append(keyword).append("%' or nmn.online =").
                    append(keyword);
        }
        return sql.toString();
    }
}
