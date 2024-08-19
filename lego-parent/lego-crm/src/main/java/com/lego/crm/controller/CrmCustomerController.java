package com.lego.crm.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.flowable.FlowableCheckStatus;
import com.lego.core.util.ExcelUtil;
import com.lego.core.vo.GenericSearchVO;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.crm.dto.CrmCustomerInfo;
import com.lego.crm.service.ICrmCustomerService;
import com.lego.crm.vo.CrmCustomerCreateVO;
import com.lego.crm.vo.CrmCustomerModifyVO;

import cn.dev33.satoken.annotation.SaCheckPermission;

@RestController
@RequestMapping("/back-end/crm-customer")
public class CrmCustomerController extends BaseController {

    @Autowired
    private ICrmCustomerService customerService;

    @PostMapping("/add")
    @SaCheckPermission("crm_customer_add")
    public JsonResponse<Object> add(@RequestBody CrmCustomerCreateVO vo) {
        String code = customerService.add(getLoginCode(), vo);
        customerService.updateCheckStatus(code, FlowableCheckStatus.completed);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    @SaCheckPermission("crm_customer_update")
    public JsonResponse<Object> update(@RequestBody CrmCustomerModifyVO vo) {
        customerService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete")
    @SaCheckPermission("crm_customer_delete")
    public JsonResponse<Object> delete(@RequestBody List<String> codes) {
        customerService.delete(getLoginCode(), codes);
        return JsonResponse.success();
    }

    @PostMapping("/list")
    @SaCheckPermission("crm_customer_read")
    public JsonResponse<LegoPage<CrmCustomerInfo>> list(@RequestBody GenericSearchVO vo) {
        return JsonResponse.success(customerService.findPageBy(vo));
    }

    @GetMapping("/get/{code}")
    @SaCheckPermission("crm_customer_detail")
    public JsonResponse<CrmCustomerInfo> getByCode(@PathVariable String code) {
        return JsonResponse.success(customerService.findBy(code));
    }

    @GetMapping("/get-simple/{code}")
    @SaCheckPermission("crm_contract_detail")
    public JsonResponse<TypeInfo> getSimpleByCode(@PathVariable String code) {
        return JsonResponse.success(customerService.findSimpleBy(code));
    }

    @PostMapping("/export")
    @SaCheckPermission("crm_customer_export")
    public void exportAll(@RequestBody List<String> codes, HttpServletResponse response) {
        List<CrmCustomerInfo> datas = customerService.findBy(codes);
        ExcelUtil.exportExcel(datas, "客户数据", CrmCustomerInfo.class, response);
    }

    @PostMapping("/export-all")
    @SaCheckPermission("crm_customer_export")
    public void exportAll(@RequestBody GenericSearchVO vo, HttpServletResponse response) {
        List<CrmCustomerInfo> datas = customerService.findBy(vo);
        ExcelUtil.exportExcel(datas, "客户数据", CrmCustomerInfo.class, response);
    }

}
