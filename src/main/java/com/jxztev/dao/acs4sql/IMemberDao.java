package com.jxztev.dao.acs4sql;

import com.jxztev.entity.acs4sql.Member;

import java.util.List;

public interface IMemberDao {
     boolean add(final Member member);
     boolean add(final List<Member> list);
     void delete(String key);
     boolean update(final Member member);
     Member get(final String keyId);
}
