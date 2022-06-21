package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author lfy
* @description 针对表【base_attr_info(属性表)】的数据库操作Service实现
* @createDate 2022-06-21 09:01:27
*/
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
    implements BaseAttrInfoService{

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueService baseAttrValueService;


    @Override
    public List<BaseAttrInfo> getBaseAttrInfoWithValue(Long c1Id, Long c2Id, Long c3Id) {

        return baseAttrInfoMapper.getBaseAttrInfoWithValue(c1Id,c2Id,c3Id);
    }

    @Transactional
    @Override
    public void saveAttrAndValue(BaseAttrInfo baseAttrInfo) {
        //1、属性名信息保存
        baseAttrInfoMapper.insert(baseAttrInfo);
        //mybatis-plus自动回填自增id到原来的JavaBean中
        Long id = baseAttrInfo.getId();


        //2、属性值信息保存
        List<BaseAttrValue> valueList = baseAttrInfo.getAttrValueList();
        for (BaseAttrValue value : valueList) {
            value.setAttrId(id);
        }
        //批量保留
        baseAttrValueService.saveBatch(valueList);

    }

    @Override
    public void updateAttrAndValue(BaseAttrInfo baseAttrInfo) {
        //1、修改属性名(名，分类，层级)。
        baseAttrInfoMapper.updateById(baseAttrInfo);


        //2、修改属性值
        List<Long> ids = new ArrayList<>();
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for (BaseAttrValue value : attrValueList) {
            //2.1、新增(没带id,直接新增)
            if(value.getId() == null){
                //回填属性id
                value.setAttrId(baseAttrInfo.getId());
                baseAttrValueService.save(value);
            }
            //2.2、修改(带了id，但是值要变化)
            if(value.getId() != null){
                baseAttrValueService.updateById(value);
                ids.add(value.getId());
            }
        }

        //2.3、删除(前端没带的值id，就是删除)
        //1、查出12原来是 59,60,61
        //2、前端带的id   60,61
        //3、计算差集：  59
        // delete * from base_attr_value
        // where attr_id=12 and id not in(60,61)
        if(ids.size() > 0){ //删除前端带的范围外的数据
            QueryWrapper<BaseAttrValue> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("attr_id",baseAttrInfo.getId());
            deleteWrapper.notIn("id",ids); //不在前端携带的范围内的都删除
            baseAttrValueService.remove(deleteWrapper);
        }else { //代表要全删
            QueryWrapper<BaseAttrValue> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("attr_id",baseAttrInfo.getId());
            baseAttrValueService.remove(deleteWrapper);
        }


    }
}




