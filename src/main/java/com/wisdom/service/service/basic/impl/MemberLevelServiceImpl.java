package com.wisdom.service.service.basic.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wisdom.common.entity.PageInfo;
import com.wisdom.dao.entity.MemberLevel;
import com.wisdom.dao.entity.MemberLevelExample;
import com.wisdom.dao.entity.MemberLevelExample.Criteria;
import com.wisdom.dao.mapper.MemberLevelMapper;
import com.wisdom.service.service.basic.IMemberLevelService;

@Service
@Transactional
public class MemberLevelServiceImpl implements IMemberLevelService {
	@Autowired
	private MemberLevelMapper memberLevelMapper;
	
	/**
	 * 列表数据
	 * @param MemberLevel
	 * @param PageInfo
	 * @return PageInfo
	 */
	public PageInfo list(MemberLevel memberLevel, PageInfo pageInfo) {
		
		memberLevel.setIsDel("0");
		MemberLevelExample example = this.getCondition(memberLevel);
		
		if(null != pageInfo && null != pageInfo.getPageStart()){
			example.setLimitClauseStart(pageInfo.getPageStart());
			example.setLimitClauseCount(pageInfo.getPageCount());
		}
		
		List<MemberLevel> list = memberLevelMapper.selectByExample(example);
		Integer count = memberLevelMapper.countByExample(example);
		
		pageInfo.setTotalCount(count);
		pageInfo.setList(list);
		
		return pageInfo;
	}
	
	/**
	 * 新增等级
	 */
	public int add(MemberLevel memberLevel) {
		memberLevel.setCreateTime(new Timestamp(new Date().getTime()));
		memberLevel.setIsDel("0");
		memberLevelMapper.insert(memberLevel);
		return memberLevel.getId();
	}
	
	/**
	 * 根据主键获取对象
	 */
	public MemberLevel get(Integer id) {
		
		return memberLevelMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 修改等级对象
	 */
	public void editMemberLevel(MemberLevel memberLevel) {
		memberLevelMapper.updateByPrimaryKeySelective(memberLevel);
	}
	
	/**
	 * 根据id删除等级对象
	 */
	public void deleteMemberLevel(Integer id) {
		memberLevelMapper.deleteByPrimaryKey(id);
	}
	/**
	 * 组装查询条件
	 */
	
	private MemberLevelExample getCondition(MemberLevel memberLevel){
		MemberLevelExample example = new MemberLevelExample();
		if(null != memberLevel){
			Criteria c = example.createCriteria();
			if(StringUtils.isNotBlank(memberLevel.getName())){
				c.andNameLike(memberLevel.getName());
			}
			if(StringUtils.isNotBlank(memberLevel.getDescrption())){
				c.andDescrptionLike(memberLevel.getDescrption());
			}
			if(StringUtils.isNotBlank(memberLevel.getIsDel())){
				c.andIsDelEqualTo(memberLevel.getIsDel());
			}
			example.setOrderByClause("create_time asc");
		}
		return example;
	}
	
	
}
