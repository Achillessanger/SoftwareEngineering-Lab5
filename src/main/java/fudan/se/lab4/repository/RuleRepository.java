package fudan.se.lab4.repository;

import fudan.se.lab4.entity.Rule;

import java.util.List;

public interface RuleRepository {
    /**
     *
     * @return 数据库中读取的1行rule对象
     */
    Rule getRule(String[] item);
    List<Rule> getRulesFromCSV(String path);
}
