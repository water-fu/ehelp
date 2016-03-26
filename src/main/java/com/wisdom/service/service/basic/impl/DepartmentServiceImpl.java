package com.wisdom.service.service.basic.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.print.attribute.standard.PageRanges;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wisdom.common.entity.PageInfo;
import com.wisdom.common.util.Pinyin4jUtil;
import com.wisdom.dao.entity.Department;
import com.wisdom.dao.entity.DepartmentExample;
import com.wisdom.dao.entity.Hospital;
import com.wisdom.dao.mapper.DepartmentMapper;
import com.wisdom.service.service.basic.IDepartmentService;

/**
 * 科室分类
 * 
 * @author
 *
 */
@Service
@Transactional
public class DepartmentServiceImpl implements IDepartmentService {
	@Autowired
	private DepartmentMapper departmentMapper;

	@Override
	public PageInfo list(Department department, PageInfo pageInfo) {
		department.setIsDel("0");
		DepartmentExample departmentExample = new DepartmentExample();

		if (null != pageInfo && pageInfo.getPageStart() != null) {
			departmentExample.setLimitClauseCount(pageInfo.getPageCount());
			departmentExample.setLimitClauseStart(pageInfo.getPageStart());
		}

		List<Department> depList = departmentMapper.selectByExample(departmentExample);
		Integer totalCount = departmentMapper.countByExample(departmentExample);

		pageInfo.setList(depList);
		pageInfo.setTotalCount(totalCount);

		return pageInfo;
	}

	@Override
	public int addDepartment(Department department) {
		department.setSimplePinyin(Pinyin4jUtil.translate(department.getName(), Pinyin4jUtil.RET_PINYIN_TYPE_HEADCHAR));
		department.setIsDel("0");
		department.setCreateTime(new Timestamp(new Date().getTime()));

		departmentMapper.insertSelective(department);

		return department.getId();
	}

	@Override
	public Department get(Integer id) {
		Department department = departmentMapper.selectByPrimaryKey(id);
		return department;
	}

	@Override
	public void editDepartment(Department department) {
		departmentMapper.updateByPrimaryKeySelective(department);

	}

	@Override
	public void deleteDepartment(Integer id) {
		Department department = new Department();

		department.setId(id);
		department.setIsDel("1");

		departmentMapper.updateByPrimaryKeySelective(department);

	}

}
