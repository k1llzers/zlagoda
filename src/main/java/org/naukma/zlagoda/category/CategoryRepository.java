package org.naukma.zlagoda.category;

import org.naukma.zlagoda.abstraction.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CategoryRepository extends BaseRepository<CategoryEntity, Integer> {

    public CategoryRepository() {
        super("INSERT INTO category (category_name) VALUES (?)",
                "UPDATE category SET category_name=? WHERE category_number=?",
                "DELETE FROM category WHERE category_number=?",
                "SELECT * FROM category WHERE category_number=?",
                "SELECT * FROM category");
    }
    @Override
    protected void setMainFields(PreparedStatement statement, CategoryEntity entity) throws SQLException {
        statement.setString(1, entity.getName());
    }

    @Override
    protected CategoryEntity parseSetToEntity(ResultSet set) throws SQLException {
        return CategoryEntity.builder()
                .id(set.getInt("category_number"))
                .name(set.getString("category_name"))
                .build();
    }

    @Override
    protected void setIdToEntity(CategoryEntity entity, ResultSet set) throws SQLException {
        entity.setId(set.getInt(1));
    }

    @Override
    protected void setIdToFindDeleteStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void setIdToUpdateStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(2, id);
    }
}
