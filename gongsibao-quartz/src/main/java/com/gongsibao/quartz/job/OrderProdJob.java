package com.gongsibao.quartz.job;

import com.gongsibao.common.util.DateUtils;
import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.order.soorderprodtrace.entity.SoOrderProdTrace;
import com.gongsibao.module.order.soorderprodtrace.service.SoOrderProdTraceService;
import com.gongsibao.module.product.prodworkflownode.entity.ProdWorkflowNode;
import com.gongsibao.module.product.prodworkflownode.service.ProdWorkflowNodeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by ys on 16/5/10.
 */
public class OrderProdJob {

    private static Logger log = Logger.getLogger(OrderProdJob.class);

    @Autowired
    private SoOrderProdService soOrderProdService;
    @Autowired
    private ProdWorkflowNodeService prodWorkflowNodeService;
    @Autowired
    private SoOrderProdTraceService soOrderProdTraceService;

    public static final int PAGE_SIZE = 1000;

    private final static List<String> WORKING_WEEK_DAY = new ArrayList<String>() {{
        add("星期一");
        add("星期二");
        add("星期三");
        add("星期四");
        add("星期五");
    }};

    private final static List<String> REST_WEEK_DAY = new ArrayList<String>() {{
        add("星期六");
        add("星期日");
    }};

    // 2016年周一到周五当中的休息日
    private final static List<String> REST_DAY_2016 = new ArrayList<String>() {{
        add("2016-01-01");
        add("2016-02-08");
        add("2016-02-09");
        add("2016-02-10");
        add("2016-02-11");
        add("2016-02-12");
        add("2016-04-04");
        add("2016-05-02");
        add("2016-06-09");
        add("2016-06-10");
        add("2016-09-15");
        add("2016-09-16");
        add("2016-10-03");
        add("2016-10-04");
        add("2016-10-05");
        add("2016-10-06");
        add("2016-10-07");
    }};
    // 2016年周末当中的工作日
    private final static List<String> WORKING_DAY_2016 = new ArrayList<String>() {{
        add("2016-02-06");
        add("2016-02-14");
        add("2016-06-12");
        add("2016-09-18");
        add("2016-10-08");
        add("2016-10-09");
    }};


    public void execute() throws InterruptedException {
        // 当前时间
        String currentDate = DateUtils.dateStr(new Date());
        log.info("++++++++++ currentDate is: " + currentDate + "++++++++++");
        // 周末且周末不上班的情况 不处理
        if(REST_WEEK_DAY.contains(DateUtils.getWeekOfDate(new Date())) && !WORKING_DAY_2016.contains(currentDate)) {
            return;
        }
        // 周一到周五且休息的情况 不处理
        if(WORKING_WEEK_DAY.contains(DateUtils.getWeekOfDate(new Date())) && REST_DAY_2016.contains(currentDate)) {
            return;
        }

        log.info("++++++++++ run start ++++++++++");

        // 当前页
        int currentPage = 1;
        // 总页数
        int totalPages = 0;
        Map<String, Object> condition = new HashMap<>();
        /** 105 审核状态：1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过 */
        condition.put("noAuditStatusId", 1054);

        int traceTimeoutDays = 0;
        int processedDays = 0;
        int needDays = 0;
        int timeoutDays = 0;
        Map<String, Integer> days = null;
        Pager<SoOrderProd> pager = null;
        List<SoOrderProd> list = null;
        boolean falg = true;
        while (falg) {
            log.info("++++++++++ currentPage is: " + currentPage + "++++++++++");

            pager = soOrderProdService.findOrderProdListByJobProperties(condition, currentPage, PAGE_SIZE);
            // 总页数
            totalPages = pager.getTotalPages();
            if(totalPages <= 0) {
                break;
            }
            list = pager.getList();
            if(CollectionUtils.isEmpty(list)) {
                break;
            }

            for(SoOrderProd orderProd : list) {
                try{
                    // 获取当前产品订单的已办理天数、需要天数、超时天数
                    days = getOrderProdDays(orderProd.getPkid(), orderProd.getProcessStatusId());
                    processedDays = NumberUtils.toInt(days.get("sumProcessdDays"));
                    needDays = NumberUtils.toInt(days.get("needDays"));
                    timeoutDays = NumberUtils.toInt(days.get("timeoutDays"));

                    // 获取当前状态超时天数
                    traceTimeoutDays = getTraceTimeoutDays(getTraceDays(orderProd.getProcessStatusId()), getTraceProcessdDays(orderProd.getPkid()));

                    // 修改天数
                    soOrderProdService.updateJobDays(orderProd.getPkid(), processedDays, needDays, timeoutDays, traceTimeoutDays);
                } catch(Exception e) {
                    log.error("++++++++++ orderProd id is: " + orderProd.getPkid() + "++++++++++");
                }
            }

            currentPage++;
            falg = (currentPage > totalPages) ? false : true;
        }
        log.info("++++++++++ run end ++++++++++");
    }


    /**
     * 获取当前状态要求天数
     *
     * @param processStatusId
     * @return 当前状态要求天数
     */
    private int getTraceDays(Integer processStatusId) {
        // 获取当前产品订单中的当前节点
        ProdWorkflowNode node = prodWorkflowNodeService.findById(processStatusId);
        if (node == null) {
            return 0;
        }
        // 计算当前状态要求天数
        return node.getWeekdayCount();
    }

    /**
     * 获取当前状态已办理天数
     *
     * @param orderProdId
     * @return 当前状态已办理天数
     */
    private int getTraceProcessdDays(Integer orderProdId) {
        SoOrderProdTrace orderProdTrace = soOrderProdTraceService.findLatestStatusByOrderProdId(orderProdId);
        if (orderProdTrace == null) {
            return 0;
        }
        // 计算当前状态停留天数 把今天需要算的一天加上
        return orderProdTrace.getProcessdDays() + 1;
    }

    /**
     * 获取当前状态超时天数
     *
     * @param traceDays,traceProcessdDays
     * @return 当前状态超时天数
     */
    private int getTraceTimeoutDays(int traceDays, int traceProcessdDays) {
        if(traceDays <= 0 || traceProcessdDays <= 0 || traceDays >= traceProcessdDays) {
            return 0;
        }
        return traceProcessdDays - traceDays;
    }

    /**
     * 获取当前产品订单的已办理天数、需要天数、超时天数
     *
     * @param orderProdId
     * @return 当前产品订单的已办理天数、需要天数、超时天数
     */
    private Map<String, Integer> getOrderProdDays(Integer orderProdId, Integer processStatusId) {
        Map<String, Integer> result = new HashMap<>();

        List<ProdWorkflowNode> list = prodWorkflowNodeService.queryWorkflowNodeListByOrderProdId(orderProdId);
        if(CollectionUtils.isEmpty(list)) {
            result.put("needDays", 0);
            result.put("sumProcessdDays", 0);
            result.put("timeoutDays", 0);
            return result;
        }

        List<Integer> ids = new ArrayList<>();
        // 需要办理的总天数
        int needDays = 0;
        for(ProdWorkflowNode workflowNode : list) {
            /** 206 产品处理流程节点类型：2061 开始、2062 结束、2063 暂停、2064 结算 */
            if (workflowNode.getTypeId().compareTo(2063) != 0) {
                needDays += workflowNode.getWeekdayCount();
                ids.add(workflowNode.getPkid());
            }
        }

        // 一共办理的天数
        int sumProcessdDays = 0;
        if(!CollectionUtils.isEmpty(ids)) {
            Map<String, Object> properties = new HashMap<>();
            properties.put("order_prod_id", orderProdId);
            properties.put("order_prod_status_id", ids);
            sumProcessdDays = soOrderProdTraceService.queryTraceProcessdDays(properties);

            // 获取当前产品订单中的当前节点 不是暂停状态 则需要加1
            ProdWorkflowNode node = prodWorkflowNodeService.findById(processStatusId);
            if (node != null && node.getTypeId().compareTo(2063) != 0) {
                sumProcessdDays += 1;
            }
        }

        // 一共超时的天数
        int timeoutDays = 0;
        if((sumProcessdDays - needDays) > 0) {
            timeoutDays = sumProcessdDays - needDays;
        }
        result.put("needDays", needDays);
        result.put("sumProcessdDays", sumProcessdDays);
        result.put("timeoutDays", timeoutDays);
        return result;
    }
}