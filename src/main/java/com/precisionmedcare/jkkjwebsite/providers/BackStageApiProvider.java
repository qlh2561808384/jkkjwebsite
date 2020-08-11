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
//        String userId = map.get("userId").toString();
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
     public String getOneUser(Map<String, Object> map){
        String userId = map.get("userId").toString();
         return "select user.id userid,\n" +
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
                 "  and user.email <> 'admin'" +
                 " and user.id = " +
                 userId;
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
                "       nmn.view_num    viewNum,\n" +
                "       nmn.detailed_drawing    detailedDrawing,\n" +
                "       nmn.quarterly_discount    quarterlyDiscount,\n" +
                "       nmn.annual_discount   annualDiscount\n" +
                "from nmn_nmn nmn\n" +
                "where status = 0");
        if(!"".equals(keyword)){
            sql.
                    append(" and (nmn.title like '%").
                    append(keyword).
                    append("%' or nmn.online = ").
                    append(keyword).append(")");
        }
        return sql.toString();
    }

    public String queryOrder(Map<String, Object> map){
        String keyword = map.get("keyword").toString();
        String userId = map.get("userId").toString();
        StringBuilder sql = new StringBuilder();
        sql.append("select nmn.id,\n" +
                "       nmn.out_trade_no  outTradeNo,\n" +
                "       nmn.state,\n" +
                "       nmn.create_time   createTime,\n" +
                "       nmn.notify_time   notifyTime,\n" +
                "       nmn.total_fee     totalFee,\n" +
                "       nmn.nmn_id        nmnId,\n" +
                "       nmn.nmn_title     nmnTitle,\n" +
                "       nmn.nmn_img       nmnImg,\n" +
                "       nmn.user_id       userId,\n" +
                "       user.email        userEmail,\n" +
                "       nmn.ip,\n" +
                "       nmn.del,\n" +
                "       nmn.status,\n" +
                "       nmn.payment_types paymentTypes,\n" +
                "       nmn.phone,\n" +
                "       nmn.email        receiveEmail,\n" +
                "       nmn.idcard,\n" +
                "       nmn.address,\n" +
                "       nmn.receiver_name receiverName,\n" +
                "       nmn.amount amount,\n" +
                "       nmnnmn.summary\n" +
                "from nmn_nmn_order nmn\n" +
                "         left join nmn_user user on nmn.user_id = user.id\n" +
                "         left join nmn_nmn nmnnmn on nmn.nmn_id = nmnnmn.id\n" +
                "where nmn.del = 0");
        if (!"".equals(userId)) {
            sql.append(" and user.id = ").append(userId);
        }
        if(!"".equals(keyword)){
            sql.
                    append(" and (nmn.status like '%").
                    append(keyword).
                    append("%' or nmn.phone like '%").
                    append(keyword).
                    append("%' or nmn.email like '%").
                    append(keyword).
                    append("%' or nmn.out_trade_no like '%").
                    append(keyword).
                    append("%' or user.email  like '%").
                    append(keyword).
                    append("%' or nmn.idcard  like '%").
                    append(keyword).
                    append("%' or nmn.address  like '%").
                    append(keyword).
                   /* append("%' or user.id  = ").
                    append(keyword).*/
                    append("%')");
        }
        return sql.toString();
    }

    public String selectOneOrderById(Map<String, Object> map){
        String orderId = map.get("orderId").toString();
        return "SELECT nmn_nmn_order.id,\n" +
                "       nmn_nmn_order.out_trade_no,\n" +
                "       nmn_nmn_order.state,\n" +
                "       nmn_nmn_order.create_time,\n" +
                "       nmn_nmn_order.notify_time,\n" +
                "       nmn_nmn_order.total_fee,\n" +
                "       nmn_nmn_order.nmn_id,\n" +
                "       nmn_nmn_order.nmn_title,\n" +
                "       nmn_nmn_order.nmn_img,\n" +
                "       nmn_nmn_order.user_id,\n" +
                "       nmn_nmn_order.ip,\n" +
                "       nmn_nmn_order.del,\n" +
                "       nmn_nmn_order.status,\n" +
                "       nmn_nmn_order.payment_types,\n" +
                "       nmn_nmn_order.phone,\n" +
                "       nmn_nmn_order.email,\n" +
                "       nmn_nmn_order.idcard,\n" +
                "       nmn_nmn_order.address,\n" +
                "       nmn_nmn_order.receiver_name,\n" +
                "       nmn_nmn_order.amount,\n" +
                "       nmn_nmn.summary\n" +
                "FROM nmn_nmn_order\n" +
                "         left join nmn_nmn on nmn_nmn_order.nmn_id = nmn_nmn.id\n" +
                "WHERE nmn_nmn_order.del = 0 " +
                " and nmn_nmn_order.id = " + orderId;
    }
}
