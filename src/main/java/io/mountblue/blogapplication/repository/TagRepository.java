package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t.name FROM Tag t")
    public List<String> getAllTagNames();
    public Tag findByName(String tagName);

    @Query("SELECT t FROM Tag t WHERE t.id IN :Ids")
    public List<Tag> findByIds(List<Long> Ids);
}
