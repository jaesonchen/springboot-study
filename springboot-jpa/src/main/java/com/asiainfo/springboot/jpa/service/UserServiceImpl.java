package com.asiainfo.springboot.jpa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.asiainfo.springboot.jpa.model.QueryParam;
import com.asiainfo.springboot.jpa.model.User;
import com.asiainfo.springboot.jpa.repository.UserRepository;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月12日 下午1:11:43
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public List<User> findByPassword(String password) {
        return repository.findByPassword(password);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(Long id) {
        Optional<User> op = repository.findById(id);
        return op.isPresent() ? op.get() : null;
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // 简单分页
    @Override
    public Page<User> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        return repository.findAll(pageable);
    }

    // 带查询条件的分页
    @SuppressWarnings("serial")
    @Override
    public Page<User> findAll(Integer page, Integer size, final QueryParam param) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        
        return repository.findAll(new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(param.getUsername())) {
                    list.add(criteriaBuilder.equal(root.get("username").as(String.class), param.getUsername()));
                }
                if (!StringUtils.isEmpty(param.getPassword())) {
                    list.add(criteriaBuilder.equal(root.get("password").as(String.class), param.getPassword()));
                }
                Predicate[] predicates = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(predicates));
            }}, pageable);
    }
}
