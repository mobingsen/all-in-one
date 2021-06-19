package com.drop.leaves.mybatisdynamicsqlhub.module.person.service;

import com.drop.leaves.mybatisdynamicsqlhub.module.person.mapper.PersonMapper;
import com.drop.leaves.mybatisdynamicsqlhub.module.person.mapper.PersonWithAddress;
import com.drop.leaves.mybatisdynamicsqlhub.module.person.mapper.PersonWithAddressMapper;
import com.drop.leaves.mybatisdynamicsqlhub.module.person.model.LastName;
import com.drop.leaves.mybatisdynamicsqlhub.module.person.model.PersonRecord;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.drop.leaves.mybatisdynamicsqlhub.module.person.mapper.PersonDynamicSqlSupport.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

class PersonMapperTest {

    private static final String JDBC_URL = "jdbc:hsqldb:mem:aname";
    private static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";

    private SqlSessionFactory sqlSessionFactory;

    @BeforeEach
    void setup() throws Exception {
        Class.forName(JDBC_DRIVER);
        InputStream is = getClass().getResourceAsStream("/examples/simple/CreateSimpleDB.sql");
        try (Connection connection = DriverManager.getConnection(JDBC_URL, "sa", "")) {
            ScriptRunner sr = new ScriptRunner(connection);
            sr.setLogWriter(null);
            sr.runScript(new InputStreamReader(is));
        }

        UnpooledDataSource ds = new UnpooledDataSource(JDBC_DRIVER, JDBC_URL, "sa", "");
        Environment environment = new Environment("test", new JdbcTransactionFactory(), ds);
        Configuration config = new Configuration(environment);
        config.addMapper(PersonMapper.class);
        config.addMapper(PersonWithAddressMapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
    }

    @Test
    void testSelect() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            List<PersonRecord> rows = mapper.select(c ->
                    c.where(id, isEqualTo(1))
                    .or(occupation, isNull()));

            Assertions.assertThat(rows).hasSize(3);
        }
    }

    @Test
    void testSelectEmployed() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            List<PersonRecord> rows = mapper.select(c ->
                    c.where(employed, isTrue())
                    .orderBy(id));

            Assertions.assertThat(rows).hasSize(4);
            Assertions.assertThat(rows.get(0).getId()).isEqualTo(1);
        }
    }

    @Test
    void testSelectUnemployed() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            List<PersonRecord> rows = mapper.select(c ->
                    c.where(employed, isFalse())
                            .orderBy(id));

            Assertions.assertThat(rows).hasSize(2);
            Assertions.assertThat(rows.get(0).getId()).isEqualTo(3);
        }
    }

    @Test
    void testSelectWithTypeConversion() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            List<PersonRecord> rows = mapper.select(c -> c.where(id, isEqualTo(1)).or(occupation, isNull()));

            Assertions.assertThat(rows).hasSize(3);
        }
    }

    @Test
    void testSelectWithTypeConversionAndFilterAndNull() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            List<PersonRecord> rows = mapper.select(c -> c.where(id, isEqualTo((Integer) null)).or(occupation, isNull()));

            Assertions.assertThat(rows).hasSize(2);
        }
    }

    // this example is in the quick start documentation...
    @Test
    void testGeneralSelect() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            SelectStatementProvider selectStatement = select(id.as("A_ID"), firstName, lastName, birthDate, employed,
                        occupation, addressId)
                    .from(person)
                    .where(id, isEqualTo(1))
                    .or(occupation, isNull())
                    .build()
                    .render(RenderingStrategies.MYBATIS3);

            List<PersonRecord> rows = mapper.selectMany(selectStatement);
            Assertions.assertThat(rows).hasSize(3);
        }
    }

    @Test
    void testSelectAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            List<PersonRecord> rows = mapper.select(SelectDSLCompleter.allRows());

            Assertions.assertThat(rows).hasSize(6);
            Assertions.assertThat(rows.get(0).getId()).isEqualTo(1);
            Assertions.assertThat(rows.get(5).getId()).isEqualTo(6);
        }
    }

    @Test
    void testSelectAllOrdered() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            List<PersonRecord> rows = mapper
                    .select(SelectDSLCompleter.allRowsOrderedBy(lastName.descending(), firstName.descending()));

            Assertions.assertThat(rows).hasSize(6);
            Assertions.assertThat(rows.get(0).getId()).isEqualTo(5);
            Assertions.assertThat(rows.get(5).getId()).isEqualTo(1);
        }
    }

    @Test
    void testSelectDistinct() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            List<PersonRecord> rows = mapper.selectDistinct(c ->
                    c.where(id, isGreaterThan(1))
                    .or(occupation, isNull()));

            Assertions.assertThat(rows).hasSize(5);
        }
    }

    @Test
    void testSelectWithTypeHandler() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            List<PersonRecord> rows = mapper.select(c ->
                    c.where(employed, isEqualTo(false))
                    .orderBy(id));

            assertAll(
                    () -> Assertions.assertThat(rows).hasSize(2),
                    () -> Assertions.assertThat(rows.get(0).getId()).isEqualTo(3),
                    () -> Assertions.assertThat(rows.get(1).getId()).isEqualTo(6)
            );
        }
    }

    @Test
    void testSelectByPrimaryKeyWithMissingRecord() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            Optional<PersonRecord> record = mapper.selectByPrimaryKey(300);
            Assertions.assertThat(record).isNotPresent();
        }
    }

    @Test
    void testFirstNameIn() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            List<PersonRecord> rows = mapper.select(c ->
                    c.where(firstName, isIn("Fred", "Barney")));

            assertAll(
                    () -> Assertions.assertThat(rows).hasSize(2),
                    () -> Assertions.assertThat(rows.get(0).getLastName().getName()).isEqualTo("Flintstone"),
                    () -> Assertions.assertThat(rows.get(1).getLastName().getName()).isEqualTo("Rubble")
            );
        }
    }

    @Test
    void testDelete() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            int rows = mapper.delete(c ->
                    c.where(occupation, isNull()));
            Assertions.assertThat(rows).isEqualTo(2);
        }
    }

    // this test is in the quick start documentation
    @Test
    void testGeneralDelete() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            DeleteStatementProvider deleteStatement = deleteFrom(person)
                    .where(occupation, isNull())
                    .build()
                    .render(RenderingStrategies.MYBATIS3);

            int rows = mapper.delete(deleteStatement);
            Assertions.assertThat(rows).isEqualTo(2);
        }
    }

    @Test
    void testDeleteAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            int rows = mapper.delete(DeleteDSLCompleter.allRows());

            Assertions.assertThat(rows).isEqualTo(6);
        }
    }

    @Test
    void testDeleteByPrimaryKey() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            int rows = mapper.deleteByPrimaryKey(2);

            Assertions.assertThat(rows).isEqualTo(1);
        }
    }

    @Test
    void testInsert() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            PersonRecord record = new PersonRecord();
            record.setId(100);
            record.setFirstName("Joe");
            record.setLastName(LastName.of("Jones"));
            record.setBirthDate(new Date());
            record.setEmployed(true);
            record.setOccupation("Developer");
            record.setAddressId(1);

            int rows = mapper.insert(record);
            Assertions.assertThat(rows).isEqualTo(1);
        }
    }

    @Test
    void testGeneralInsert() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            int rows = mapper.generalInsert(c ->
                c.set(id).toValue(100)
                .set(firstName).toValue("Joe")
                .set(lastName).toValue(LastName.of("Jones"))
                .set(birthDate).toValue(new Date())
                .set(employed).toValue(true)
                .set(occupation).toValue("Developer")
                .set(addressId).toValue(1)
            );

            Assertions.assertThat(rows).isEqualTo(1);
        }
    }

    @Test
    void testInsertMultiple() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            List<PersonRecord> records = new ArrayList<>();

            PersonRecord record = new PersonRecord();
            record.setId(100);
            record.setFirstName("Joe");
            record.setLastName(LastName.of("Jones"));
            record.setBirthDate(new Date());
            record.setEmployed(true);
            record.setOccupation("Developer");
            record.setAddressId(1);
            records.add(record);

            record = new PersonRecord();
            record.setId(101);
            record.setFirstName("Sarah");
            record.setLastName(LastName.of("Smith"));
            record.setBirthDate(new Date());
            record.setEmployed(true);
            record.setOccupation("Architect");
            record.setAddressId(2);
            records.add(record);

            int rows = mapper.insertMultiple(records);
            Assertions.assertThat(rows).isEqualTo(2);
        }
    }

    @Test
    void testInsertSelective() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            PersonRecord record = new PersonRecord();
            record.setId(100);
            record.setFirstName("Joe");
            record.setLastName(LastName.of("Jones"));
            record.setBirthDate(new Date());
            record.setEmployed(false);
            record.setAddressId(1);

            int rows = mapper.insertSelective(record);
            Assertions.assertThat(rows).isEqualTo(1);
        }
    }

    @Test
    void testUpdateByPrimaryKey() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            PersonRecord record = new PersonRecord();
            record.setId(100);
            record.setFirstName("Joe");
            record.setLastName(LastName.of("Jones"));
            record.setBirthDate(new Date());
            record.setEmployed(true);
            record.setOccupation("Developer");
            record.setAddressId(1);

            int rows = mapper.insert(record);
            Assertions.assertThat(rows).isEqualTo(1);

            record.setOccupation("Programmer");
            rows = mapper.updateByPrimaryKey(record);
            Assertions.assertThat(rows).isEqualTo(1);

            Optional<PersonRecord> newRecord = mapper.selectByPrimaryKey(100);
            Assertions.assertThat(newRecord).isPresent();
            Assertions.assertThat(newRecord.get().getOccupation()).isEqualTo("Programmer");
        }
    }

    @Test
    void testUpdateByPrimaryKeySelective() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            PersonRecord record = new PersonRecord();
            record.setId(100);
            record.setFirstName("Joe");
            record.setLastName(LastName.of("Jones"));
            record.setBirthDate(new Date());
            record.setEmployed(true);
            record.setOccupation("Developer");
            record.setAddressId(1);

            int rows = mapper.insert(record);
            Assertions.assertThat(rows).isEqualTo(1);

            PersonRecord updateRecord = new PersonRecord();
            updateRecord.setId(100);
            updateRecord.setOccupation("Programmer");
            rows = mapper.updateByPrimaryKeySelective(updateRecord);
            Assertions.assertThat(rows).isEqualTo(1);

            Optional<PersonRecord> newRecord = mapper.selectByPrimaryKey(100);
            Assertions.assertThat(newRecord).isPresent();
            Assertions.assertThat(newRecord.get().getOccupation()).isEqualTo("Programmer");
            Assertions.assertThat(newRecord.get().getFirstName()).isEqualTo("Joe");
        }
    }

    @Test
    void testUpdate() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            PersonRecord record = new PersonRecord();
            record.setId(100);
            record.setFirstName("Joe");
            record.setLastName(LastName.of("Jones"));
            record.setBirthDate(new Date());
            record.setEmployed(true);
            record.setOccupation("Developer");
            record.setAddressId(1);

            int rows = mapper.insert(record);
            Assertions.assertThat(rows).isEqualTo(1);

            record.setOccupation("Programmer");

            rows = mapper.update(c ->
                PersonMapper.updateAllColumns(record, c)
                .where(id, isEqualTo(100))
                .and(firstName, isEqualTo("Joe")));

            Assertions.assertThat(rows).isEqualTo(1);

            Optional<PersonRecord> newRecord = mapper.selectByPrimaryKey(100);
            Assertions.assertThat(newRecord).isPresent();
            Assertions.assertThat(newRecord.get().getOccupation()).isEqualTo("Programmer");
        }
    }

    @Test
    void testUpdateOneField() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            PersonRecord record = new PersonRecord();
            record.setId(100);
            record.setFirstName("Joe");
            record.setLastName(LastName.of("Jones"));
            record.setBirthDate(new Date());
            record.setEmployed(true);
            record.setOccupation("Developer");
            record.setAddressId(1);

            int rows = mapper.insert(record);
            Assertions.assertThat(rows).isEqualTo(1);

            rows = mapper.update(c ->
                c.set(occupation).equalTo("Programmer")
                .where(id, isEqualTo(100)));

            Assertions.assertThat(rows).isEqualTo(1);

            Optional<PersonRecord> newRecord = mapper.selectByPrimaryKey(100);
            Assertions.assertThat(newRecord).isPresent();
            Assertions.assertThat(newRecord.get().getOccupation()).isEqualTo("Programmer");
        }
    }

    @Test
    void testUpdateAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            PersonRecord record = new PersonRecord();
            record.setId(100);
            record.setFirstName("Joe");
            record.setLastName(LastName.of("Jones"));
            record.setBirthDate(new Date());
            record.setEmployed(true);
            record.setOccupation("Developer");
            record.setAddressId(1);

            int rows = mapper.insert(record);
            Assertions.assertThat(rows).isEqualTo(1);

            PersonRecord updateRecord = new PersonRecord();
            updateRecord.setOccupation("Programmer");
            rows = mapper.update(c ->
                PersonMapper.updateSelectiveColumns(updateRecord, c));

            Assertions.assertThat(rows).isEqualTo(7);

            Optional<PersonRecord> newRecord = mapper.selectByPrimaryKey(100);
            Assertions.assertThat(newRecord).isPresent();
            Assertions.assertThat(newRecord.get().getOccupation()).isEqualTo("Programmer");
        }
    }

    @Test
    void testUpdateSelective() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            PersonRecord record = new PersonRecord();
            record.setId(100);
            record.setFirstName("Joe");
            record.setLastName(LastName.of("Jones"));
            record.setBirthDate(new Date());
            record.setEmployed(true);
            record.setOccupation("Developer");
            record.setAddressId(1);

            int rows = mapper.insert(record);
            Assertions.assertThat(rows).isEqualTo(1);

            PersonRecord updateRecord = new PersonRecord();
            updateRecord.setOccupation("Programmer");
            rows = mapper.update(c ->
                PersonMapper.updateSelectiveColumns(updateRecord, c)
                .where(id, isEqualTo(100)));

            Assertions.assertThat(rows).isEqualTo(1);

            Optional<PersonRecord> newRecord = mapper.selectByPrimaryKey(100);
            Assertions.assertThat(newRecord).isPresent();
            Assertions.assertThat(newRecord.get().getOccupation()).isEqualTo("Programmer");
        }
    }

    @Test
    void testCount() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            long rows = mapper.count(c ->
                    c.where(occupation, isNull()));

            Assertions.assertThat(rows).isEqualTo(2L);
        }
    }

    @Test
    void testCountAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            long rows = mapper.count(CountDSLCompleter.allRows());

            Assertions.assertThat(rows).isEqualTo(6L);
        }
    }

    @Test
    void testCountLastName() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            long rows = mapper.count(lastName, CountDSLCompleter.allRows());

            Assertions.assertThat(rows).isEqualTo(6L);
        }
    }

    @Test
    void testCountDistinctLastName() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            long rows = mapper.countDistinct(lastName, CountDSLCompleter.allRows());

            Assertions.assertThat(rows).isEqualTo(2L);
        }
    }

    @Test
    void testTypeHandledLike() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            List<PersonRecord> rows = mapper.select(c ->
                    c.where(lastName, isLike(LastName.of("Fl%")))
                    .orderBy(id));

            Assertions.assertThat(rows).hasSize(3);
            Assertions.assertThat(rows.get(0).getFirstName()).isEqualTo("Fred");
        }
    }

    @Test
    void testTypeHandledNotLike() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);

            List<PersonRecord> rows = mapper.select(c ->
                    c.where(lastName, isNotLike(LastName.of("Fl%")))
                    .orderBy(id));

            Assertions.assertThat(rows).hasSize(3);
            Assertions.assertThat(rows.get(0).getFirstName()).isEqualTo("Barney");
        }
    }

    @Test
    void testJoinAllRows() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonWithAddressMapper mapper = session.getMapper(PersonWithAddressMapper.class);
            List<PersonWithAddress> records = mapper.select(
                    SelectDSLCompleter.allRowsOrderedBy(id)
            );

            Assertions.assertThat(records).hasSize(6);
            Assertions.assertThat(records.get(0).getId()).isEqualTo(1);
            Assertions.assertThat(records.get(0).getEmployed()).isTrue();
            Assertions.assertThat(records.get(0).getFirstName()).isEqualTo("Fred");
            Assertions.assertThat(records.get(0).getLastName()).isEqualTo(LastName.of("Flintstone"));
            Assertions.assertThat(records.get(0).getOccupation()).isEqualTo("Brontosaurus Operator");
            Assertions.assertThat(records.get(0).getBirthDate()).isNotNull();
            Assertions.assertThat(records.get(0).getAddress().getId()).isEqualTo(1);
            Assertions.assertThat(records.get(0).getAddress().getStreetAddress()).isEqualTo("123 Main Street");
            Assertions.assertThat(records.get(0).getAddress().getCity()).isEqualTo("Bedrock");
            Assertions.assertThat(records.get(0).getAddress().getState()).isEqualTo("IN");
        }
    }

    @Test
    void testJoinOneRow() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonWithAddressMapper mapper = session.getMapper(PersonWithAddressMapper.class);
            List<PersonWithAddress> records = mapper.select(c -> c.where(id, isEqualTo(1)));

            Assertions.assertThat(records).hasSize(1);
            Assertions.assertThat(records.get(0).getId()).isEqualTo(1);
            Assertions.assertThat(records.get(0).getEmployed()).isTrue();
            Assertions.assertThat(records.get(0).getFirstName()).isEqualTo("Fred");
            Assertions.assertThat(records.get(0).getLastName()).isEqualTo(LastName.of("Flintstone"));
            Assertions.assertThat(records.get(0).getOccupation()).isEqualTo("Brontosaurus Operator");
            Assertions.assertThat(records.get(0).getBirthDate()).isNotNull();
            Assertions.assertThat(records.get(0).getAddress().getId()).isEqualTo(1);
            Assertions.assertThat(records.get(0).getAddress().getStreetAddress()).isEqualTo("123 Main Street");
            Assertions.assertThat(records.get(0).getAddress().getCity()).isEqualTo("Bedrock");
            Assertions.assertThat(records.get(0).getAddress().getState()).isEqualTo("IN");
        }
    }

    @Test
    void testJoinPrimaryKey() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonWithAddressMapper mapper = session.getMapper(PersonWithAddressMapper.class);
            Optional<PersonWithAddress> record = mapper.selectByPrimaryKey(1);

            Assertions.assertThat(record).hasValueSatisfying(r -> {
                Assertions.assertThat(r.getId()).isEqualTo(1);
                Assertions.assertThat(r.getEmployed()).isTrue();
                Assertions.assertThat(r.getFirstName()).isEqualTo("Fred");
                Assertions.assertThat(r.getLastName()).isEqualTo(LastName.of("Flintstone"));
                Assertions.assertThat(r.getOccupation()).isEqualTo("Brontosaurus Operator");
                Assertions.assertThat(r.getBirthDate()).isNotNull();
                Assertions.assertThat(r.getAddress().getId()).isEqualTo(1);
                Assertions.assertThat(r.getAddress().getStreetAddress()).isEqualTo("123 Main Street");
                Assertions.assertThat(r.getAddress().getCity()).isEqualTo("Bedrock");
                Assertions.assertThat(r.getAddress().getState()).isEqualTo("IN");
            });
        }
    }

    @Test
    void testJoinPrimaryKeyInvalidRecord() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonWithAddressMapper mapper = session.getMapper(PersonWithAddressMapper.class);
            Optional<PersonWithAddress> record = mapper.selectByPrimaryKey(55);

            Assertions.assertThat(record).isEmpty();
        }
    }

    @Test
    void testJoinCount() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonWithAddressMapper mapper = session.getMapper(PersonWithAddressMapper.class);
            long count = mapper.count(c -> c.where(person.id, isEqualTo(55)));

            Assertions.assertThat(count).isZero();
        }
    }

    @Test
    void testJoinCountWithSubcriteria() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PersonWithAddressMapper mapper = session.getMapper(PersonWithAddressMapper.class);
            long count = mapper.count(c -> c.where(person.id, isEqualTo(55), or(person.id, isEqualTo(1))));

            Assertions.assertThat(count).isEqualTo(1);
        }
    }
}