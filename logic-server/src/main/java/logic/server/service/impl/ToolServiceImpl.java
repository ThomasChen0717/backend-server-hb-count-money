package logic.server.service.impl;

import logic.server.dto.*;
import logic.server.repository.*;
import logic.server.service.IToolService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class ToolServiceImpl implements IToolService {

    @Autowired
    private CfgGlobalRepository cfgGlobalRepository;
    @Autowired
    private CfgVehicleRepository cfgVehicleRepository;
    @Autowired
    private CfgVehicleNewRepository cfgVehicleNewRepository;
    @Autowired
    private CfgAttributeRepository cfgAttributeRepository;
    @Autowired
    private CfgEquipmentRepository cfgEquipmentRepository;
    @Autowired
    private CfgBuffToolRepository cfgBuffToolRepository;
    @Autowired
    private CfgMagnateRepository cfgMagnateRepository;
    @Autowired
    private CfgBossRepository cfgBossRepository;
    @Autowired
    private CfgVipRepository cfgVipRepository;


    @Override
    public List<String> updateFromExcel(String path, List<String> fileNames) {
        List<String> errorFiles = new ArrayList<String>();
        for (String fileName : fileNames) {
            try {
                //本地excel文档转化为File
                File file = new File(path + fileName);
                //读取本地excel文档
                FileInputStream inputStream = new FileInputStream(file);
                Workbook workBook = new XSSFWorkbook(inputStream);
                //默认单页excel文档
                Sheet sheet = workBook.getSheetAt(0);
                int firstRow = sheet.getFirstRowNum();
                int lastRow = sheet.getLastRowNum();
                int success;
                switch (fileName) {
                    case "t_cfg_attribute.xlsx":
                        success = updateCfgAttribute(sheet, firstRow, lastRow);
                        if (success == -1) {
                            errorFiles.add(fileName + ":更新失败！");
                            log.error("ToolServiceImpl::Update Failed:更新失败");
                        }
                        break;
                    case "t_cfg_boss.xlsx":
                        success = updateCfgBoss(sheet, firstRow, lastRow);
                        if (success == -1) {
                            errorFiles.add(fileName + ":更新失败！");
                            log.error("ToolServiceImpl::Update Failed:更新失败");
                        }
                        break;
                    case "t_cfg_buff_tool.xlsx":
                        success = updateCfgBuffTool(sheet, firstRow, lastRow);
                        if (success == -1) {
                            errorFiles.add(fileName + ":更新失败！");
                            log.error("ToolServiceImpl::Update Failed:更新失败");
                        }
                        break;
                    case "t_cfg_equipment.xlsx":
                        success = updateCfgEquipment(sheet, firstRow, lastRow);
                        if (success == -1) {
                            errorFiles.add(fileName + ":更新失败！");
                            log.error("ToolServiceImpl::Update Failed:更新失败");
                        }
                        break;
                    case "t_cfg_global.xlsx":
                        success = updateCfgGlobal(sheet, firstRow, lastRow);
                        if (success == -1) {
                            errorFiles.add(fileName + ":更新失败！");
                            log.error("ToolServiceImpl::Update Failed:更新失败");
                        }
                        break;
                    case "t_cfg_magnate.xlsx":
                        success = updateCfgMagnate(sheet, firstRow, lastRow);
                        if (success == -1) {
                            errorFiles.add(fileName + ":更新失败！");
                            log.error("ToolServiceImpl::Update Failed:更新失败");
                        }
                        break;
                    case "t_cfg_vehicle.xlsx":
                        success = updateCfgVehicle(sheet, firstRow, lastRow);
                        if (success == -1) {
                            errorFiles.add(fileName + ":更新失败！");
                            log.error("ToolServiceImpl::Update Failed:更新失败");
                        }
                        break;
                    case "t_cfg_vehicle_new.xlsx":
                        success = updateCfgVehicleNew(sheet, firstRow, lastRow);
                        if (success == -1) {
                            errorFiles.add(fileName + ":更新失败！");
                            log.error("ToolServiceImpl::Update Failed:更新失败");
                        }
                        break;
                    case "t_cfg_vip.xlsx":
                        success = updateCfgVip(sheet, firstRow, lastRow);
                        if (success == -1) {
                            errorFiles.add(fileName + ":更新失败！");
                            log.error("ToolServiceImpl::Update Failed:更新失败");
                        }
                        break;
                    default:
                        //未找到文件
                        errorFiles.add(fileName + ":未找到对应SQL表格");
                        log.error("ToolServiceImpl::NoSQLTableFound:未找到对应SQL表格");
                }
                //结束并停止读取文件
                inputStream.close();
                //处理报错
            } catch (Exception e) {
                errorFiles.add(fileName + ":未找到Excel文件");
                log.error("ToolServiceImpl::NoExcelFileFound:未找到Excel文件");
            }
        }
        return errorFiles;
    }

    public int updateCfgAttribute(Sheet sheet, int firstRow, int lastRow) {
        try {
            //清除原表
            cfgAttributeRepository.delete();
            //循环excel表并存入
            for (int index = firstRow + 1; index <= lastRow; index++) {
                Row row = sheet.getRow(index);
                if (row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }
                CfgAttributeDTO cfgAttributeDTO = new CfgAttributeDTO();
                cfgAttributeDTO.setAttributeType((int) row.getCell(0).getNumericCellValue())
                                .setAttributeName(row.getCell(1).getStringCellValue())
                                .setAttributeLevelUpFormula(row.getCell(2).getStringCellValue())
                                .setAttributeEffectFormula(row.getCell(3).getStringCellValue());
                cfgAttributeRepository.add(cfgAttributeDTO);
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }

    public int updateCfgBoss(Sheet sheet, int firstRow, int lastRow) {
        try {
            //清除原表
            cfgBossRepository.delete();
            //循环excel表并存入
            for (int index = firstRow + 1; index <= lastRow; index++) {
                Row row = sheet.getRow(index);
                if (row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }
                CfgBossDTO cfgBossDTO = new CfgBossDTO();
                cfgBossDTO.setBossId((int) row.getCell(0).getNumericCellValue())
                          .setBossName(row.getCell(1).getStringCellValue())
                          .setSpeed((int) row.getCell(2).getNumericCellValue())
                          .setTargetMoneyAmount((int) row.getCell(3).getNumericCellValue())
                          .setRewardMoneyAmount((int) row.getCell(4).getNumericCellValue())
                          .setChallengeTime((int) row.getCell(5).getNumericCellValue())
                          .setPreBossId((int) row.getCell(6).getNumericCellValue())
                          .setShowIndex((int) row.getCell(7).getNumericCellValue())
                          .setResourceName(Integer.toString((int) row.getCell(8).getNumericCellValue()))
                          .setBossWord(row.getCell(9).getStringCellValue())
                          .setFixed((int) row.getCell(10).getNumericCellValue());
                cfgBossRepository.add(cfgBossDTO);
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }

    public int updateCfgBuffTool(Sheet sheet, int firstRow, int lastRow) {
        try {
            //清除原表
            cfgBuffToolRepository.delete();
            //循环excel表并存入
            for (int index = firstRow + 1; index <= lastRow; index++) {
                Row row = sheet.getRow(index);
                if (row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }
                CfgBuffToolDTO cfgBuffToolDTO = new CfgBuffToolDTO();
                cfgBuffToolDTO.setBuffToolId((int) row.getCell(0).getNumericCellValue())
                              .setName(row.getCell(1).getStringCellValue())
                              .setEffectAttributeInfo(row.getCell(2).getStringCellValue())
                              .setDurations((int) row.getCell(3).getNumericCellValue());
                cfgBuffToolRepository.add(cfgBuffToolDTO);
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }

    public int updateCfgEquipment(Sheet sheet, int firstRow, int lastRow) {
        try {
            //清除原表
            cfgEquipmentRepository.delete();
            //循环excel表并存入
            for (int index = firstRow + 1; index <= lastRow; index++) {
                Row row = sheet.getRow(index);
                if (row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }
                CfgEquipmentDTO cfgEquipmentDTO = new CfgEquipmentDTO();
                cfgEquipmentDTO.setEquipmentId((int) row.getCell(0).getNumericCellValue())
                               .setEquipmentName(row.getCell(1).getStringCellValue())
                               .setUnlockConditionType((int) row.getCell(2).getNumericCellValue())
                               .setUnlockConditionCount((int) row.getCell(3).getNumericCellValue())
                               .setEffectAttributeType((int) row.getCell(4).getNumericCellValue())
                               .setEffectAttributeMultiple((int) row.getCell(5).getNumericCellValue())
                               .setEffectAttributeRemark(row.getCell(6).getStringCellValue())
                               .setShowIndex((int) row.getCell(7).getNumericCellValue())
                               .setPreEquipmentId((int) row.getCell(8).getNumericCellValue())
                               .setResourceName(Integer.toString((int) row.getCell(9).getNumericCellValue()))
                               .setPreConditionChallengeType((int) row.getCell(10).getNumericCellValue())
                               .setPreConditionChallengeId((int) row.getCell(11).getNumericCellValue());
                cfgEquipmentRepository.add(cfgEquipmentDTO);
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;

    }

    public int updateCfgGlobal(Sheet sheet, int firstRow, int lastRow) {
        try {
            //清除原表
            cfgGlobalRepository.delete();
            //循环excel表并存入
            for (int index = firstRow + 1; index <= lastRow; index++) {
                Row row = sheet.getRow(index);
                if (row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }
                CfgGlobalDTO cfgGlobalDTO = new CfgGlobalDTO();
                cfgGlobalDTO.setKeyName(row.getCell(0).getStringCellValue())
                            .setValueName(new DataFormatter().formatCellValue(row.getCell(1)))
                            .setRemark(row.getCell(2).getStringCellValue());
                cfgGlobalRepository.add(cfgGlobalDTO);
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }

    public int updateCfgMagnate(Sheet sheet, int firstRow, int lastRow) {
        try {
            //清除原表
            cfgMagnateRepository.delete();
            //循环excel表并存入
            for (int index = firstRow + 1; index <= lastRow; index++) {
                Row row = sheet.getRow(index);
                if (row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }
                CfgMagnateDTO cfgMagnateDTO = new CfgMagnateDTO();
                cfgMagnateDTO.setMagnateId((int) row.getCell(0).getNumericCellValue())
                             .setMagnateName(row.getCell(1).getStringCellValue())
                             .setSpeed((int) row.getCell(2).getNumericCellValue())
                             .setTargetMoneyAmount((int) row.getCell(3).getNumericCellValue())
                             .setRewardMoneyAmount((int) row.getCell(4).getNumericCellValue())
                             .setUnlockVehicleId((int) row.getCell(5).getNumericCellValue())
                             .setCdTime((int) row.getCell(6).getNumericCellValue())
                             .setChallengeTime((int) row.getCell(7).getNumericCellValue())
                             .setPreMagnateId((int) row.getCell(8).getNumericCellValue())
                             .setShowIndex((int) row.getCell(9).getNumericCellValue())
                             .setResourceName(Integer.toString((int) row.getCell(10).getNumericCellValue()))
                             .setBossWord(row.getCell(11).getStringCellValue())
                             .setFixed((int) row.getCell(12).getNumericCellValue());
                cfgMagnateRepository.add(cfgMagnateDTO);
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }

    public int updateCfgVehicle(Sheet sheet, int firstRow, int lastRow) {
        try {
            //清除原表
            cfgVehicleRepository.delete();
            //循环excel表并存入
            for (int index = firstRow + 1; index <= lastRow; index++) {
                Row row = sheet.getRow(index);
                if (row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }
                CfgVehicleDTO cfgVehicleDTO = new CfgVehicleDTO();
                cfgVehicleDTO.setVehicleId((int) row.getCell(0).getNumericCellValue())
                             .setVehicleName(row.getCell(1).getStringCellValue())
                             .setUnlockConditionType((int) row.getCell(2).getNumericCellValue())
                             .setUnlockConditionCount((int) row.getCell(3).getNumericCellValue())
                             .setVehicleCapacity((int) row.getCell(4).getNumericCellValue())
                             .setExtraRewardValue((int) row.getCell(5).getNumericCellValue())
                             .setShowIndex((int) row.getCell(6).getNumericCellValue())
                             .setResourceName(Integer.toString((int) row.getCell(7).getNumericCellValue()));
                cfgVehicleRepository.add(cfgVehicleDTO);
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }

    public int updateCfgVehicleNew(Sheet sheet, int firstRow, int lastRow) {
        try {
            //清除原表
            cfgVehicleNewRepository.delete();
            //循环excel表并存入
            for (int index = firstRow + 1; index <= lastRow; index++) {
                Row row = sheet.getRow(index);
                if (row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }
                CfgVehicleNewDTO cfgVehicleNewDTO = new CfgVehicleNewDTO();
                cfgVehicleNewDTO.setVehicleId((int) row.getCell(0).getNumericCellValue())
                                .setVehicleName(row.getCell(1).getStringCellValue())
                                .setPreConditionChallengeType((int) row.getCell(2).getNumericCellValue())
                                .setPreConditionChallengeId((int) row.getCell(3).getNumericCellValue())
                                .setUnlockConditionType((int) row.getCell(4).getNumericCellValue())
                                .setUnlockConditionCount((int) row.getCell(5).getNumericCellValue())
                                .setShowIndex((int) row.getCell(6).getNumericCellValue())
                                .setLevelMax((int) row.getCell(7).getNumericCellValue())
                                .setLevelUpFormula(row.getCell(8).getStringCellValue())
                                .setIncomeFormula(row.getCell(9).getStringCellValue())
                                .setResourceName(row.getCell(10).getStringCellValue());
                cfgVehicleNewRepository.add(cfgVehicleNewDTO);
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }

    public int updateCfgVip(Sheet sheet, int firstRow, int lastRow) {
        try {
            //清除原表
            cfgVipRepository.delete();
            //循环excel表并存入
            for (int index = firstRow + 1; index <= lastRow; index++) {
                Row row = sheet.getRow(index);
                if (row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }
                CfgVipDTO cfgVipDTO = new CfgVipDTO();
                cfgVipDTO.setVipLevel((int) row.getCell(0).getNumericCellValue())
                         .setConditionCount((int) row.getCell(1).getNumericCellValue())
                         .setEffectAttributeInfo(row.getCell(2).getStringCellValue());
                cfgVipRepository.add(cfgVipDTO);
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }


}
