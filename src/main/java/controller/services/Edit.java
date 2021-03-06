package controller.services;

import dao.*;
import model.*;
import utils.Input;
import view.Menu;
import view.Show;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Edit {

    public static void editDeveloper(Developer developer, DeveloperDAO developerDAO, SkillDAO skillDAO, ProjectDAO projectDAO,
                                     CompanyDAO companyDAO) {
        System.out.println(developer);
        int menuCount = Menu.editMenu(developer.getClass());
        int choice = Input.getBoundIntInput("", 0, menuCount);
        while (choice != 0) {
            switch (choice) {
                case 1:
                    developer.setId(Input.getPositiveIntInput("Enter new ID"));
                    break;
                case 2:
                    developer.setFirstName(Input.getStringInputLimitNotNull(50, "Enter new first name"));
                    break;
                case 3:
                    developer.setLastName(Input.getStringInputLimitNotNull(50, "Enter new last name"));
                    break;
                case 4:
                    developer.setSalary(Input.getBigDecimalPositive("Enter new salary"));
                    break;
                case 5:
                    try {
                        List<Skill> allSkills = skillDAO.getAll();
                        Show.listAll(allSkills);
                        List<Integer> allSkillIds = Show.getIds(allSkills);
                        allSkillIds.add(0);
                        List<Integer> skillIds = Input.getAllowedIntegerList("Enter ID's of skills", allSkillIds);
                        List<Skill> skills = new ArrayList<>();
                        if (!(skillIds.size() == 1 && skillIds.get(0) == 0)) {
                            if (skillIds.contains(0)) skillIds.remove(new Integer(0));
                            for (Integer skillId : skillIds) {
                                skills.add(skillDAO.getById(skillId));
                            }
                        }

                        developer.setSkills(skills);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    break;
                case 6:
                    try {
                        List<Project> allProjects = projectDAO.getAll();
                        Show.listAll(allProjects);
                        List<Integer> allProjectIds = Show.getIds(allProjects);
                        allProjectIds.add(0);
                        List<Integer> projectIds = Input.getAllowedIntegerList("Enter ID's of projects", allProjectIds);
                        List<Project> projects = new ArrayList<>();
                        if (!(projectIds.size() == 1 && projectIds.get(0) == 0)) {
                            if (projectIds.contains(0)) projectIds.remove(new Integer(0));
                            for (Integer id : projectIds) {
                                try {
                                    projects.add(projectDAO.getById(id));
                                } catch (SQLException | NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        developer.setProjects(projects);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    try {
                        List<Company> companies = companyDAO.getAll();
                        Show.listAll(companies);
                        Company company = companyDAO.getById(Input.getPositiveIntInput("Enter company ID"));
                        while (company == null) {
                            company = companyDAO.getById(Input.getPositiveIntInput("ID not found, please re-enter!"));
                        }
                        developer.setCompany(company);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    System.out.println(developer);
                    break;
                case 9:
                    Save.saveDeveloper(developer, developerDAO);
            }
            Menu.editMenu(developer.getClass());
            choice = Input.getBoundIntInput("", 0, menuCount);
        }

    }

    public static void editProject(Project project, ProjectDAO projectDAO, CustomerDAO customerDAO, DeveloperDAO developerDAO) {
        System.out.println(project);
        int menuCount = Menu.editMenu(project.getClass());
        int choice = Input.getBoundIntInput("", 0, menuCount);
        while (choice != 0) {
            switch (choice) {
                case 1:
                    project.setId(Input.getPositiveIntInput("Enter new ID"));
                    break;
                case 2:
                    project.setName(Input.getStringInputLimitNotNull(50, "Enter new name"));
                    break;
                case 3:
                    project.setDescription(Input.getStringInputLimitNotNull(100, "Enter new description"));
                    break;
                case 4:
                    project.setCost(Input.getBigDecimalPositive("Enter new cost"));
                    break;
                case 5:
                    try {
                        List<Customer> customers = customerDAO.getAll();
                        Show.listAll(customers);
                        List<Integer> allCustomerIds = Show.getIds(customers);
                        project.setCustomer(customerDAO.getById(Input.getAllowedIntInput("Enter ID of a customer", allCustomerIds)));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    List<Developer> developers;
                    try {
                        developers = developerDAO.getAll();
                        Show.listAll(developers);
                        List<Integer> allDeveloperIds = Show.getIds(developers);
                        allDeveloperIds.add(0);
                        List<Integer> developerIds = Input.getAllowedIntegerList("Enter IDs of developers", allDeveloperIds);
                        if (!(developerIds.size() == 1 && developerIds.get(0) == 0)) {
                            if(developerIds.contains(0)) developerIds.remove(new Integer(0));
                        }
                        else developerIds.clear();
                        project.setDeveloperIds(developerIds);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                case 7:
                    System.out.println(project);
                    break;
                case 8:
                    Save.saveProject(project, projectDAO);
            }
            Menu.editMenu(project.getClass());
            choice = Input.getBoundIntInput("", 0, menuCount);
        }
    }

    public static void editSkill(Skill skill, SkillDAO skillDAO) {
        System.out.println(skill);
        int menuCount = Menu.editMenu(skill.getClass());
        int choice = Input.getBoundIntInput("", 0, menuCount);
        while (choice != 0) {
            switch (choice) {
                case 1:
                    skill.setId(Input.getPositiveIntInput("Enter new ID"));
                    break;
                case 2:
                    skill.setDescription(Input.getStringInputLimitNotNull(50, "Enter new description"));
                    break;
                case 3:
                    System.out.println(skill);
                    break;
                case 4:
                    Save.saveSkill(skill, skillDAO);
            }
            Menu.editMenu(skill.getClass());
            choice = Input.getBoundIntInput("", 0, menuCount);
        }
    }

    public static void editCompany(Company company, CompanyDAO companyDAO) {
        System.out.println(company);
        int menuCount = Menu.editMenu(company.getClass());
        int choice = Input.getBoundIntInput("", 0, menuCount);
        while (choice != 0) {
            switch (choice) {
                case 1:
                    company.setId(Input.getPositiveIntInput("Enter new ID"));
                    break;
                case 2:
                    company.setName(Input.getStringInputLimitNotNull(50, "Enter new name"));
                    break;
                case 3:
                    company.setDescription(Input.getStringInputLimitNotNull(100, "Enter new description"));
                    break;
                case 4:
                    company.setCountry(Input.getStringInputLimitNotNull(20, "Enter new country"));
                    break;
                case 5:
                    System.out.println("You cannot edit developers from here, Company for every Developer is set in Developer.");
                    break;
                case 6:
                    System.out.println(company);
                    break;
                case 7:
                    Save.saveCompany(company, companyDAO);
            }
            Menu.editMenu(company.getClass());
            choice = Input.getBoundIntInput("", 0, menuCount);
        }

    }

    public static void editCustomer(Customer customer, CustomerDAO customerDAO, ProjectDAO projectDAO) {
        System.out.println(customer);
        int menuCount = Menu.editMenu(customer.getClass());
        int choice = Input.getBoundIntInput("", 0, menuCount);
        while (choice != 0) {
            switch (choice) {
                case 1:
                    customer.setId(Input.getPositiveIntInput("Enter new ID"));
                    break;
                case 2:
                    customer.setFirstName(Input.getStringInputLimitNotNull(50, "Enter new first name"));
                    break;
                case 3:
                    customer.setLastName(Input.getStringInputLimitNotNull(50, "Enter new last name"));
                    break;
                case 4:
                    customer.setInfo(Input.getStringInputLimitNotNull(100, "Enter new info"));
                    break;
                case 5:
                    System.out.println("You cannot edit projects from here, Customer for every Project is set in Project.");
                    break;
                case 6:
                    System.out.println(customer);
                    break;
                case 7:
                    Save.saveCustomer(customer, customerDAO);
            }
            Menu.editMenu(customer.getClass());
            choice = Input.getBoundIntInput("", 0, menuCount);
        }

    }
}