package dao.jdbc;

import dao.ConnectionUtil;
import dao.ProjectDAO;
import model.Project;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcProjectDAOImpl implements ProjectDAO {
    @Override
    public Project getById(Integer id) throws SQLException {
        Statement statement = ConnectionUtil.getConnection().createStatement();
        String sql = "select * from projects where id = " + id;

        ResultSet resultSet = statement.executeQuery(sql);

        if (resultSet.next()) {
            int pId = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            BigDecimal cost = resultSet.getBigDecimal("cost");
            return new Project(pId, name, description, cost);
            }
        return null;
    }
    @Override
    public int getColumnSize(String column) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();

        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet rsColumns = databaseMetaData.getColumns(null, null, "projects", column);
        int size = 0;
        while (rsColumns.next()) {
            String columnName = rsColumns.getString("COLUMN_NAME");
            System.out.println("column name=" + columnName);
            String columnType = rsColumns.getString("TYPE_NAME");
            System.out.println("type:" + columnType);
            size = rsColumns.getInt("COLUMN_SIZE");
            System.out.println("size:" + size);
            int nullable = rsColumns.getInt("NULLABLE");
            if (nullable == DatabaseMetaData.columnNullable) {
                System.out.println("nullable true");
            } else {
                System.out.println("nullable false");
            }
            int position = rsColumns.getInt("ORDINAL_POSITION");
            System.out.println("position:" + position);

        }
        return size;
    }



    @Override
    public List<Project> getAll() throws SQLException {
        Statement statement = ConnectionUtil.getConnection().createStatement();
        String sql = "select id from projects";
        ResultSet resultSet = statement.executeQuery(sql);
        List<Integer> projIds = new ArrayList<>();
        while (resultSet.next()) {
            projIds.add(resultSet.getInt("id"));
        }
        List<Project> projects = new ArrayList<>();
        for (Integer i : projIds) {
            projects.add(getById(i));
        }
        return projects;
    }

    @Override
    public void save(Project project) {
        try
                (Statement statement = ConnectionUtil.getConnection().createStatement())
        {
            String sql = "insert into projects values ('" +
                    project.getId() + "', '" +
                    project.getName() + "', '" +
                    project.getDescription() + "', '" +
                    project.getCost() + "')";
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Project project) {
        deleteById(project.getId());
        save(project);
    }

    @Override
    public void delete(Project project) {
        deleteById(project.getId());
    }

    @Override
    public void deleteById(Integer id) {
        try
                (Statement statement = ConnectionUtil.getConnection().createStatement())
        {
            statement.addBatch("delete from projects_developers where project_id = " + id);
            statement.addBatch("delete from customers_projects where project_id = " + id);
            statement.addBatch("delete from projects where id = " + id);
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
