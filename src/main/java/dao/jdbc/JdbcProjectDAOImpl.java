package dao.jdbc;

import dao.ConnectionUtil;
import dao.ProjectDAO;
import model.Customer;
import model.Project;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcProjectDAOImpl implements ProjectDAO {
    @Override
    public Project getById(Integer id) {
        try
                (
                Connection connection = ConnectionUtil.getConnection();
                Statement statement = connection.createStatement()
                )
        {
            String sql = "select * from projects where id = " + id;

            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                int pId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                BigDecimal cost = resultSet.getBigDecimal("cost");
                Customer customer = null;
                String customerSql = "select customer_id from customers_projects where project_id = " + id;
                ResultSet customerResultSet = statement.executeQuery(customerSql);
                if (customerResultSet.next()) {
                    int cusId = customerResultSet.getInt("customer_id");
                    customer = new JdbcCustomerDAOImpl().getById(cusId);
                }
                customerResultSet.close();

                String devSql = "select developer_id from projects_developers where project_id = " + id;
                ResultSet devResultSet = statement.executeQuery(devSql);
                List<Integer> devIds = new ArrayList<>();
                while (devResultSet.next()) {
                    devIds.add(devResultSet.getInt("developer_id"));
                }

                return new Project(pId, name, description, cost, customer, devIds);
            }
            resultSet.close();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

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
        resultSet.close();
        statement.close();
        List<Project> projects = new ArrayList<>();
        for (Integer i : projIds) {
            projects.add(getById(i));
        }
        return projects;
    }

    @Override
    public void save(Project project) {
        try
                (Statement statement = ConnectionUtil.getConnection().createStatement()) {
            int id = project.getId();
            String sql = "insert into projects values ('" +
                    id + "', '" +
                    project.getName() + "', '" +
                    project.getDescription() + "', '" +
                    project.getCost() + "')";
            statement.addBatch(sql);

            sql = "insert into customers_projects values ('" + project.getCustomer().getId() + "', '" + project.getId() + "')";
            statement.addBatch(sql);

            if (project.getDeveloperIds().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("insert into projects_developers values ");
                List<Integer> developerIds = project.getDeveloperIds();
                for (Integer devId : developerIds) {
                    stringBuilder.append("('").append(id).append("', '").append(devId).append("'),");
                }
                stringBuilder.setLength(stringBuilder.length() - 1);
                statement.addBatch(stringBuilder.toString());
            }

            statement.executeBatch();
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
                (Statement statement = ConnectionUtil.getConnection().createStatement()) {
            statement.addBatch("delete from projects_developers where project_id = " + id);
            statement.addBatch("delete from customers_projects where project_id = " + id);
            statement.addBatch("delete from projects where id = " + id);
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
