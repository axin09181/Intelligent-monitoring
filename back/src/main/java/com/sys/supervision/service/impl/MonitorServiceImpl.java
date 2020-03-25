package com.sys.supervision.service.impl;

import com.sys.supervision.dao.EquipmentMapper;
import com.sys.supervision.dao.ProjectMapper;
import com.sys.supervision.entity.db.Equipment;
import com.sys.supervision.entity.db.Project;
import com.sys.supervision.enums.ProjectStatusEnum;
import com.sys.supervision.model.Location;
import com.sys.supervision.model.enhance.EquipmentEnhance;
import com.sys.supervision.service.IMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MonitorServiceImpl implements IMonitorService {

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public Map<String, Object> getMonitorInfo() {
        List<Equipment> equipmentList = equipmentMapper.getAll();
        List<Project> projectList = projectMapper.getAll();
        if (equipmentList == null) return new HashMap<>(0);

//        List<EquipmentEnhance> enhances = new ArrayList<>(equipmentList.size());
        Map<String, Integer> equipMap = new HashMap<>(16);
        Map<String, Integer> warningMap = new HashMap<>(16);
        List<Location> locationList = new ArrayList<>();
        Integer onlineNumber = 0;
        Integer offlineNumber = 0;
        Integer warning = 0;
        for (Project p : projectList) {
            // 根据城市统计预警信息
            if (ProjectStatusEnum.WARNING.getCode().equals(p.getProjectStatus())) {
                warning++;
                warningMap.put(p.getCity(), warningMap.getOrDefault(p.getCity(), 1));
            }

            // 统计在线数量
            if (ProjectStatusEnum.ON_LINE.getCode().equals(p.getProjectStatus())) {
                onlineNumber++;
            }

            // 统计离线数量
            if (ProjectStatusEnum.OFF_LINE.getCode().equals(p.getProjectStatus())) {
                offlineNumber++;
            }

            // 根据城市统计设备数量信息
            equipMap.put(p.getCity(), equipMap.getOrDefault(p.getCity(), 0) + p.getEquipmentNumnber());

            // 添加所有项目的地址
            locationList.add(Location.builder()
                    .latitude(p.getLatitude())
                    .longitude(p.getLongitude())
                    .name(p.getName())
                    .status(p.getProjectStatus())
                    .build());
        }

        Map<String, Integer> countMap = new HashMap<>(3);
        countMap.put("total", projectList.size());
        countMap.put("online", onlineNumber);
        countMap.put("offline", offlineNumber);
        countMap.put("warning", warning);

        Map<String, Object> result = new HashMap<>(2);
        result.put("equipMap", equipMap);
        result.put("warningMap", warningMap);
        result.put("locationList", locationList);
        result.put("countMap", countMap);
        return result;
    }

}