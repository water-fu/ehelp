package com.wisdom.mapp.core.configuration;

import com.wisdom.mapp.core.filter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 */
@Configuration
public class MappConfig {

	private static Logger log = LoggerFactory.getLogger(MappConfig.class);

	@Autowired
	private MappSettings mappSettings;
	
	@Autowired
	private IntegrityRequestFilter integrityRequestFilter;
	
	@Autowired
	private IpCheckFilter ipCheckFilter;
	
	@Autowired
	private UserCheckFilter userCheckFilter;
	
	@Autowired
	private RightCheckFilter rightCheckFilter;

	
//	<bean name="handlerFilterChain" class="com.ailk.butterfly.mapp.core.filter.FilterChain">
//		<property name="filters">
//			<list>
//					<ref bean="integrityRequestFilter"/>
//				<ref bean="ipCheckFilter"/>
//				<ref bean="userCheckFilter"/>
//				<ref bean="rightCheckFilter"/>
//			</list>
//		</property>
//	</bean>

	@Bean
	public FilterChain<Map<String,Object>> handlerFilterChain() {
		FilterChain<Map<String,Object>> handlerFilterChain = new FilterChain<Map<String,Object>>();
		List<IFilter<Map<String,Object>>> filters = new ArrayList<IFilter<Map<String,Object>>>(0);
		filters.add(integrityRequestFilter);
		filters.add(ipCheckFilter);
		filters.add(userCheckFilter);
		filters.add(rightCheckFilter);
		handlerFilterChain.setFilters(filters);
		return handlerFilterChain;
	}

	@PostConstruct
	public void postConstruct() {
		log.info("mapp.settings={}", mappSettings);
	}
}
