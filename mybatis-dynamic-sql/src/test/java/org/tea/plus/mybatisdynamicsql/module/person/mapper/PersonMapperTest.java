package org.tea.plus.mybatisdynamicsql.module.person.mapper;

import org.tea.plus.mybatisdynamicsql.module.person.model.LastName;
import org.tea.plus.mybatisdynamicsql.module.person.model.PersonRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

@SpringBootTest
class PersonMapperTest {

    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private PersonWithAddressMapper personWithAddressMapper;

    @Test
    void testSelect() {
        List<PersonRecord> rows = personMapper.select(c -> c.where(PersonDynamicSqlSupport.id, isEqualTo(1)).or(PersonDynamicSqlSupport.occupation, isNull()));
        assertThat(rows).hasSize(3);
    }

    @Test
    void testSelectEmployed() {
        List<PersonRecord> rows = personMapper.select(c -> c.where(PersonDynamicSqlSupport.employed, isTrue()).orderBy(PersonDynamicSqlSupport.id));
        assertThat(rows).hasSize(4);
        assertThat(rows.get(0).getId()).isEqualTo(1);
    }

    @Test
    void testSelectUnemployed() {
        List<PersonRecord> rows = personMapper.select(c -> c.where(PersonDynamicSqlSupport.employed, isFalse()).orderBy(PersonDynamicSqlSupport.id));
        assertThat(rows).hasSize(2);
        assertThat(rows.get(0).getId()).isEqualTo(3);
    }

    @Test
    void testSelectWithTypeConversion() {
        List<PersonRecord> rows = personMapper.select(c -> c.where(PersonDynamicSqlSupport.id, isEqualTo(1)).or(PersonDynamicSqlSupport.occupation, isNull()));
        assertThat(rows).hasSize(3);
    }

    @Test
    void testSelectWithTypeConversionAndFilterAndNull() {
        List<PersonRecord> rows = personMapper.select(c -> c.where(PersonDynamicSqlSupport.id, isEqualTo((Integer) null)).or(PersonDynamicSqlSupport.occupation, isNull()));
        assertThat(rows).hasSize(2);
    }

    // this example is in the quick start documentation...
    @Test
    void testGeneralSelect() {
        SelectStatementProvider selectStatement = select(PersonDynamicSqlSupport.id.as("A_ID"), PersonDynamicSqlSupport.firstName, PersonDynamicSqlSupport.lastName, PersonDynamicSqlSupport.birthDate, PersonDynamicSqlSupport.employed, PersonDynamicSqlSupport.occupation, PersonDynamicSqlSupport.addressId)
                .from(PersonDynamicSqlSupport.person)
                .where(PersonDynamicSqlSupport.id, isEqualTo(1))
                .or(PersonDynamicSqlSupport.occupation, isNull())
                .build()
                .render(RenderingStrategies.MYBATIS3);
        List<PersonRecord> rows = personMapper.selectMany(selectStatement);
        assertThat(rows).hasSize(3);
    }

    @Test
    void testSelectAll() {
        List<PersonRecord> rows = personMapper.select(SelectDSLCompleter.allRows());
        assertThat(rows).hasSize(6);
        assertThat(rows.get(0).getId()).isEqualTo(1);
        assertThat(rows.get(5).getId()).isEqualTo(6);
    }

    @Test
    void testSelectAllOrdered() {
        List<PersonRecord> rows = personMapper.select(SelectDSLCompleter.allRowsOrderedBy(PersonDynamicSqlSupport.lastName.descending(), PersonDynamicSqlSupport.firstName.descending()));
        assertThat(rows).hasSize(6);
        assertThat(rows.get(0).getId()).isEqualTo(5);
        assertThat(rows.get(5).getId()).isEqualTo(1);
    }

    @Test
    void testSelectDistinct() {
        List<PersonRecord> rows = personMapper.selectDistinct(c -> c.where(PersonDynamicSqlSupport.id, isGreaterThan(1)).or(PersonDynamicSqlSupport.occupation, isNull()));
        assertThat(rows).hasSize(5);
    }

    @Test
    void testSelectWithTypeHandler() {
        List<PersonRecord> rows = personMapper.select(c -> c.where(PersonDynamicSqlSupport.employed, isEqualTo(false)).orderBy(PersonDynamicSqlSupport.id));
        assertAll(
                () -> assertThat(rows).hasSize(2),
                () -> assertThat(rows.get(0).getId()).isEqualTo(3),
                () -> assertThat(rows.get(1).getId()).isEqualTo(6)
        );
    }

    @Test
    void testSelectByPrimaryKeyWithMissingRecord() {
        Optional<PersonRecord> record = personMapper.selectByPrimaryKey(300);
        assertThat(record).isNotPresent();
    }

    @Test
    void testFirstNameIn() {
        List<PersonRecord> rows = personMapper.select(c -> c.where(PersonDynamicSqlSupport.firstName, isIn("Fred", "Barney")));
        assertAll(
                () -> assertThat(rows).hasSize(2),
                () -> assertThat(rows.get(0).getLastName().getName()).isEqualTo("Flintstone"),
                () -> assertThat(rows.get(1).getLastName().getName()).isEqualTo("Rubble")
        );
    }

    @Test
    void testDelete() {
        int rows = personMapper.delete(c -> c.where(PersonDynamicSqlSupport.occupation, isNull()));
        assertThat(rows).isEqualTo(2);
    }

    // this test is in the quick start documentation
    @Test
    void testGeneralDelete() {
        DeleteStatementProvider deleteStatement = deleteFrom(PersonDynamicSqlSupport.person).where(PersonDynamicSqlSupport.occupation, isNull()).build()
                .render(RenderingStrategies.MYBATIS3);
        int rows = personMapper.delete(deleteStatement);
        assertThat(rows).isEqualTo(2);
    }

    @Test
    void testDeleteAll() {
        int rows = personMapper.delete(DeleteDSLCompleter.allRows());
        assertThat(rows).isEqualTo(6);
    }

    @Test
    void testDeleteByPrimaryKey() {
        int rows = personMapper.deleteByPrimaryKey(2);
        assertThat(rows).isEqualTo(1);
    }

    @Test
    void testInsert() {
        PersonRecord record = new PersonRecord();
        record.setId(100);
        record.setFirstName("Joe");
        record.setLastName(LastName.of("Jones"));
        record.setBirthDate(new Date());
        record.setEmployed(true);
        record.setOccupation("Developer");
        record.setAddressId(1);
        int rows = personMapper.insert(record);
        assertThat(rows).isEqualTo(1);
    }

    @Test
    void testGeneralInsert() {
        int rows = personMapper.generalInsert(c ->
                c.set(PersonDynamicSqlSupport.id).toValue(100)
                        .set(PersonDynamicSqlSupport.firstName).toValue("Joe")
                        .set(PersonDynamicSqlSupport.lastName).toValue(LastName.of("Jones"))
                        .set(PersonDynamicSqlSupport.birthDate).toValue(new Date())
                        .set(PersonDynamicSqlSupport.employed).toValue(true)
                        .set(PersonDynamicSqlSupport.occupation).toValue("Developer")
                        .set(PersonDynamicSqlSupport.addressId).toValue(1)
        );
        assertThat(rows).isEqualTo(1);
    }

    @Test
    void testInsertMultiple() {
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
        int rows = personMapper.insertMultiple(records);
        assertThat(rows).isEqualTo(2);
    }

    @Test
    void testInsertSelective() {
        PersonRecord record = new PersonRecord();
        record.setId(100);
        record.setFirstName("Joe");
        record.setLastName(LastName.of("Jones"));
        record.setBirthDate(new Date());
        record.setEmployed(false);
        record.setAddressId(1);
        int rows = personMapper.insertSelective(record);
        assertThat(rows).isEqualTo(1);
    }

    @Test
    void testUpdateByPrimaryKey() {
        PersonRecord record = new PersonRecord();
        record.setId(100);
        record.setFirstName("Joe");
        record.setLastName(LastName.of("Jones"));
        record.setBirthDate(new Date());
        record.setEmployed(true);
        record.setOccupation("Developer");
        record.setAddressId(1);
        int rows = personMapper.insert(record);
        assertThat(rows).isEqualTo(1);
        record.setOccupation("Programmer");
        rows = personMapper.updateByPrimaryKey(record);
        assertThat(rows).isEqualTo(1);
        Optional<PersonRecord> newRecord = personMapper.selectByPrimaryKey(100);
        assertThat(newRecord).isPresent();
        assertThat(newRecord.get().getOccupation()).isEqualTo("Programmer");
    }

    @Test
    void testUpdateByPrimaryKeySelective() {
        PersonRecord record = new PersonRecord();
        record.setId(100);
        record.setFirstName("Joe");
        record.setLastName(LastName.of("Jones"));
        record.setBirthDate(new Date());
        record.setEmployed(true);
        record.setOccupation("Developer");
        record.setAddressId(1);
        int rows = personMapper.insert(record);
        assertThat(rows).isEqualTo(1);
        PersonRecord updateRecord = new PersonRecord();
        updateRecord.setId(100);
        updateRecord.setOccupation("Programmer");
        rows = personMapper.updateByPrimaryKeySelective(updateRecord);
        assertThat(rows).isEqualTo(1);
        Optional<PersonRecord> newRecord = personMapper.selectByPrimaryKey(100);
        assertThat(newRecord).isPresent();
        assertThat(newRecord.get().getOccupation()).isEqualTo("Programmer");
        assertThat(newRecord.get().getFirstName()).isEqualTo("Joe");
    }

    @Test
    void testUpdate() {
        PersonRecord record = new PersonRecord();
        record.setId(100);
        record.setFirstName("Joe");
        record.setLastName(LastName.of("Jones"));
        record.setBirthDate(new Date());
        record.setEmployed(true);
        record.setOccupation("Developer");
        record.setAddressId(1);
        int rows = personMapper.insert(record);
        assertThat(rows).isEqualTo(1);
        record.setOccupation("Programmer");
        rows = personMapper.update(c -> PersonMapper.updateAllColumns(record, c)
                .where(PersonDynamicSqlSupport.id, isEqualTo(100))
                .and(PersonDynamicSqlSupport.firstName, isEqualTo("Joe")));
        assertThat(rows).isEqualTo(1);
        Optional<PersonRecord> newRecord = personMapper.selectByPrimaryKey(100);
        assertThat(newRecord).isPresent();
        assertThat(newRecord.get().getOccupation()).isEqualTo("Programmer");
    }

    @Test
    void testUpdateOneField() {
        PersonRecord record = new PersonRecord();
        record.setId(100);
        record.setFirstName("Joe");
        record.setLastName(LastName.of("Jones"));
        record.setBirthDate(new Date());
        record.setEmployed(true);
        record.setOccupation("Developer");
        record.setAddressId(1);
        int rows = personMapper.insert(record);
        assertThat(rows).isEqualTo(1);
        rows = personMapper.update(c -> c.set(PersonDynamicSqlSupport.occupation).equalTo("Programmer").where(PersonDynamicSqlSupport.id, isEqualTo(100)));
        assertThat(rows).isEqualTo(1);
        Optional<PersonRecord> newRecord = personMapper.selectByPrimaryKey(100);
        assertThat(newRecord).isPresent();
        assertThat(newRecord.get().getOccupation()).isEqualTo("Programmer");
    }

    @Test
    void testUpdateAll() {
        PersonRecord record = new PersonRecord();
        record.setId(100);
        record.setFirstName("Joe");
        record.setLastName(LastName.of("Jones"));
        record.setBirthDate(new Date());
        record.setEmployed(true);
        record.setOccupation("Developer");
        record.setAddressId(1);
        int rows = personMapper.insert(record);
        assertThat(rows).isEqualTo(1);
        PersonRecord updateRecord = new PersonRecord();
        updateRecord.setOccupation("Programmer");
        rows = personMapper.update(c -> PersonMapper.updateSelectiveColumns(updateRecord, c));
        assertThat(rows).isEqualTo(7);
        Optional<PersonRecord> newRecord = personMapper.selectByPrimaryKey(100);
        assertThat(newRecord).isPresent();
        assertThat(newRecord.get().getOccupation()).isEqualTo("Programmer");
    }

    @Test
    void testUpdateSelective() {
        PersonRecord record = new PersonRecord();
        record.setId(100);
        record.setFirstName("Joe");
        record.setLastName(LastName.of("Jones"));
        record.setBirthDate(new Date());
        record.setEmployed(true);
        record.setOccupation("Developer");
        record.setAddressId(1);
        int rows = personMapper.insert(record);
        assertThat(rows).isEqualTo(1);
        PersonRecord updateRecord = new PersonRecord();
        updateRecord.setOccupation("Programmer");
        rows = personMapper.update(c -> PersonMapper.updateSelectiveColumns(updateRecord, c).where(PersonDynamicSqlSupport.id, isEqualTo(100)));
        assertThat(rows).isEqualTo(1);
        Optional<PersonRecord> newRecord = personMapper.selectByPrimaryKey(100);
        assertThat(newRecord).isPresent();
        assertThat(newRecord.get().getOccupation()).isEqualTo("Programmer");
    }

    @Test
    void testCount() {
        long rows = personMapper.count(c -> c.where(PersonDynamicSqlSupport.occupation, isNull()));
        assertThat(rows).isEqualTo(2L);
    }

    @Test
    void testCountAll() {
        long rows = personMapper.count(CountDSLCompleter.allRows());
        assertThat(rows).isEqualTo(6L);
    }

    @Test
    void testCountLastName() {
        long rows = personMapper.count(PersonDynamicSqlSupport.lastName, CountDSLCompleter.allRows());
        assertThat(rows).isEqualTo(6L);
    }

    @Test
    void testCountDistinctLastName() {
        long rows = personMapper.countDistinct(PersonDynamicSqlSupport.lastName, CountDSLCompleter.allRows());
        assertThat(rows).isEqualTo(2L);
    }

    @Test
    void testTypeHandledLike() {
        List<PersonRecord> rows = personMapper.select(c -> c.where(PersonDynamicSqlSupport.lastName, isLike(LastName.of("Fl%"))).orderBy(PersonDynamicSqlSupport.id));
        assertThat(rows).hasSize(3);
        assertThat(rows.get(0).getFirstName()).isEqualTo("Fred");
    }

    @Test
    void testTypeHandledNotLike() {
        List<PersonRecord> rows = personMapper.select(c -> c.where(PersonDynamicSqlSupport.lastName, isNotLike(LastName.of("Fl%"))).orderBy(PersonDynamicSqlSupport.id));
        assertThat(rows).hasSize(3);
        assertThat(rows.get(0).getFirstName()).isEqualTo("Barney");
    }

    @Test
    void testJoinAllRows() {
        List<PersonWithAddress> records = personWithAddressMapper.select(SelectDSLCompleter.allRowsOrderedBy(PersonDynamicSqlSupport.id));
        Assertions.assertThat(records).hasSize(6);
        assertThat(records.get(0).getId()).isEqualTo(1);
        assertThat(records.get(0).getEmployed()).isTrue();
        assertThat(records.get(0).getFirstName()).isEqualTo("Fred");
        Assertions.assertThat(records.get(0).getLastName()).isEqualTo(LastName.of("Flintstone"));
        assertThat(records.get(0).getOccupation()).isEqualTo("Brontosaurus Operator");
        assertThat(records.get(0).getBirthDate()).isNotNull();
        Assertions.assertThat(records.get(0).getAddress().getId()).isEqualTo(1);
        Assertions.assertThat(records.get(0).getAddress().getStreetAddress()).isEqualTo("123 Main Street");
        Assertions.assertThat(records.get(0).getAddress().getCity()).isEqualTo("Bedrock");
        Assertions.assertThat(records.get(0).getAddress().getState()).isEqualTo("IN");
    }

    @Test
    void testJoinOneRow() {
        List<PersonWithAddress> records = personWithAddressMapper.select(c -> c.where(PersonDynamicSqlSupport.id, isEqualTo(1)));
        Assertions.assertThat(records).hasSize(1);
        assertThat(records.get(0).getId()).isEqualTo(1);
        assertThat(records.get(0).getEmployed()).isTrue();
        assertThat(records.get(0).getFirstName()).isEqualTo("Fred");
        Assertions.assertThat(records.get(0).getLastName()).isEqualTo(LastName.of("Flintstone"));
        assertThat(records.get(0).getOccupation()).isEqualTo("Brontosaurus Operator");
        assertThat(records.get(0).getBirthDate()).isNotNull();
        Assertions.assertThat(records.get(0).getAddress().getId()).isEqualTo(1);
        Assertions.assertThat(records.get(0).getAddress().getStreetAddress()).isEqualTo("123 Main Street");
        Assertions.assertThat(records.get(0).getAddress().getCity()).isEqualTo("Bedrock");
        Assertions.assertThat(records.get(0).getAddress().getState()).isEqualTo("IN");
    }

    @Test
    void testJoinPrimaryKey() {
        Optional<PersonWithAddress> record = personWithAddressMapper.selectByPrimaryKey(1);
        Assertions.assertThat(record).hasValueSatisfying(r -> {
            assertThat(r.getId()).isEqualTo(1);
            assertThat(r.getEmployed()).isTrue();
            assertThat(r.getFirstName()).isEqualTo("Fred");
            Assertions.assertThat(r.getLastName()).isEqualTo(LastName.of("Flintstone"));
            assertThat(r.getOccupation()).isEqualTo("Brontosaurus Operator");
            assertThat(r.getBirthDate()).isNotNull();
            Assertions.assertThat(r.getAddress().getId()).isEqualTo(1);
            Assertions.assertThat(r.getAddress().getStreetAddress()).isEqualTo("123 Main Street");
            Assertions.assertThat(r.getAddress().getCity()).isEqualTo("Bedrock");
            Assertions.assertThat(r.getAddress().getState()).isEqualTo("IN");
        });
    }

    @Test
    void testJoinPrimaryKeyInvalidRecord() {
        Optional<PersonWithAddress> record = personWithAddressMapper.selectByPrimaryKey(55);
        Assertions.assertThat(record).isEmpty();
    }

    @Test
    void testJoinCount() {
        long count = personWithAddressMapper.count(c -> c.where(PersonDynamicSqlSupport.person.id, isEqualTo(55)));
        assertThat(count).isZero();
    }

    @Test
    void testJoinCountWithSubcriteria() {
        long count = personWithAddressMapper.count(c -> c.where(PersonDynamicSqlSupport.person.id, isEqualTo(55), or(PersonDynamicSqlSupport.person.id, isEqualTo(1))));
        assertThat(count).isEqualTo(1);
    }
}
