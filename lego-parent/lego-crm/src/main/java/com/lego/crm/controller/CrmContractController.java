package com.lego.crm.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.core.flowable.FlowableCheckStatus;
import com.lego.core.util.ExcelUtil;
import com.lego.core.vo.GenericSearchVO;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.crm.dto.CrmContractInfo;
import com.lego.crm.service.ICrmContractService;
import com.lego.crm.vo.CrmContractCreateVO;
import com.lego.crm.vo.CrmContractModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/back-end/crm-contract")
public class CrmContractController extends BaseController {

    @Autowired
    private ICrmContractService contractService;

    @PostMapping("/add")
    @SaCheckPermission("crm_contract_add")
    public JsonResponse<Object> add(@RequestBody CrmContractCreateVO vo) {
        contractService.add(getLoginCode(), vo);
        contractService.updateCheckStatus(vo.getCode(), FlowableCheckStatus.completed);
        return JsonResponse.success();
    }

    @PostMapping("/update")
    @SaCheckPermission("crm_contract_update")
    public JsonResponse<Object> update(@RequestBody CrmContractModifyVO vo) {
        contractService.update(getLoginCode(), vo);
        return JsonResponse.success();
    }

    @PostMapping("/delete")
    @SaCheckPermission("crm_contract_delete")
    public JsonResponse<Object> delete(@RequestBody List<String> codes) {
        contractService.delete(getLoginCode(), codes);
        return JsonResponse.success();
    }

    @PostMapping("/list")
    @SaCheckPermission("crm_contract_read")
    public JsonResponse<LegoPage<CrmContractInfo>> list(@RequestBody GenericSearchVO vo) {
        return JsonResponse.success(contractService.findPageBy(vo));
    }

    @GetMapping("/get/{code}")
    @SaCheckPermission("crm_contract_detail")
    public JsonResponse<CrmContractInfo> getByCode(@PathVariable String code) {
        return JsonResponse.success(contractService.findBy(code));
    }

    @GetMapping("/get-simple/{code}")
    @SaCheckPermission("crm_contract_detail")
    public JsonResponse<TypeInfo> getSimpleByCode(@PathVariable String code) {
        return JsonResponse.success(contractService.findSimpleBy(code));
    }

    @PostMapping("/export")
    @SaCheckPermission("crm_contract_export")
    public void exportAll(@RequestBody List<String> codes, HttpServletResponse response) {
        List<CrmContractInfo> datas = contractService.findBy(codes);
        ExcelUtil.exportExcel(datas, "合同数据", CrmContractInfo.class, response);
    }

    @PostMapping("/export-all")
    @SaCheckPermission("crm_contract_export")
    public void exportAll(@RequestBody GenericSearchVO vo, HttpServletResponse response) {
        List<CrmContractInfo> datas = contractService.findBy(vo);
        ExcelUtil.exportExcel(datas, "合同数据", CrmContractInfo.class, response);
    }

}
