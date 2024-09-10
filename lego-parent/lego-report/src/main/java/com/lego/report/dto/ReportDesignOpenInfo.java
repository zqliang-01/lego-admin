package com.lego.report.dto;

import com.lego.core.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReportDesignOpenInfo<M> extends DTO {

    private List<M> results = new ArrayList<M>();
    private List<ReportTitleInfo> titles = new ArrayList<>();
}
