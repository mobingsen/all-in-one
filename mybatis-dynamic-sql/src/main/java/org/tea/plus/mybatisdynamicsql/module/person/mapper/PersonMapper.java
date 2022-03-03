package org.tea.plus.mybatisdynamicsql.module.person.mapper;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import org.tea.plus.mybatisdynamicsql.module.person.handler.LastNameTypeHandler;
import org.tea.plus.mybatisdynamicsql.module.person.handler.YesNoTypeHandler;
import org.tea.plus.mybatisdynamicsql.module.person.model.PersonRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.insert.GeneralInsertDSL;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

/**
 * Note: this is the canonical mapper with the new style methods
 * and represents the desired output for MyBatis Generator
 *
 * @author mobingsen
 */
@Mapper
public interface PersonMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<PersonRecord>, CommonUpdateMapper {

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @Results(id = "PersonResult", value = {
            @Result(column = "A_ID", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "first_name", property = "firstName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "last_name", property = "lastName", jdbcType = JdbcType.VARCHAR, typeHandler = LastNameTypeHandler.class),
            @Result(column = "birth_date", property = "birthDate", jdbcType = JdbcType.DATE),
            @Result(column = "employed", property = "employed", jdbcType = JdbcType.VARCHAR, typeHandler = YesNoTypeHandler.class),
            @Result(column = "occupation", property = "occupation", jdbcType = JdbcType.VARCHAR),
            @Result(column = "address_id", property = "addressId", jdbcType = JdbcType.INTEGER)
    })
    List<PersonRecord> selectMany(SelectStatementProvider selectStatement);

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultMap("PersonResult")
    Optional<PersonRecord> selectOne(SelectStatementProvider selectStatement);

    BasicColumn[] selectList = BasicColumn.columnList(PersonDynamicSqlSupport.id.as("A_ID"), PersonDynamicSqlSupport.firstName, PersonDynamicSqlSupport.lastName, PersonDynamicSqlSupport.birthDate, PersonDynamicSqlSupport.employed, PersonDynamicSqlSupport.occupation, PersonDynamicSqlSupport.addressId);

    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, PersonDynamicSqlSupport.person, completer);
    }

    default long count(BasicColumn column, CountDSLCompleter completer) {
        return MyBatis3Utils.count(this::count, column, PersonDynamicSqlSupport.person, completer);
    }

    default long countDistinct(BasicColumn column, CountDSLCompleter completer) {
        return MyBatis3Utils.countDistinct(this::count, column, PersonDynamicSqlSupport.person, completer);
    }

    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, PersonDynamicSqlSupport.person, completer);
    }

    default int deleteByPrimaryKey(Integer id_) {
        return delete(c ->
                c.where(PersonDynamicSqlSupport.id, isEqualTo(id_))
        );
    }

    default int generalInsert(UnaryOperator<GeneralInsertDSL> completer) {
        return MyBatis3Utils.generalInsert(this::generalInsert, PersonDynamicSqlSupport.person, completer);
    }

    default int insert(PersonRecord record) {
        return MyBatis3Utils.insert(this::insert, record, PersonDynamicSqlSupport.person, c ->
                c.map(PersonDynamicSqlSupport.id).toProperty("id")
                        .map(PersonDynamicSqlSupport.firstName).toProperty("firstName")
                        .map(PersonDynamicSqlSupport.lastName).toProperty("lastName")
                        .map(PersonDynamicSqlSupport.birthDate).toProperty("birthDate")
                        .map(PersonDynamicSqlSupport.employed).toProperty("employed")
                        .map(PersonDynamicSqlSupport.occupation).toProperty("occupation")
                        .map(PersonDynamicSqlSupport.addressId).toProperty("addressId")
        );
    }

    default int insertMultiple(PersonRecord... records) {
        return insertMultiple(Arrays.asList(records));
    }

    default int insertMultiple(Collection<PersonRecord> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, PersonDynamicSqlSupport.person, c ->
                c.map(PersonDynamicSqlSupport.id).toProperty("id")
                        .map(PersonDynamicSqlSupport.firstName).toProperty("firstName")
                        .map(PersonDynamicSqlSupport.lastName).toProperty("lastName")
                        .map(PersonDynamicSqlSupport.birthDate).toProperty("birthDate")
                        .map(PersonDynamicSqlSupport.employed).toProperty("employed")
                        .map(PersonDynamicSqlSupport.occupation).toProperty("occupation")
                        .map(PersonDynamicSqlSupport.addressId).toProperty("addressId")
        );
    }

    default int insertSelective(PersonRecord record) {
        return MyBatis3Utils.insert(this::insert, record, PersonDynamicSqlSupport.person, c ->
                c.map(PersonDynamicSqlSupport.id).toPropertyWhenPresent("id", record::getId)
                        .map(PersonDynamicSqlSupport.firstName).toPropertyWhenPresent("firstName", record::getFirstName)
                        .map(PersonDynamicSqlSupport.lastName).toPropertyWhenPresent("lastName", record::getLastName)
                        .map(PersonDynamicSqlSupport.birthDate).toPropertyWhenPresent("birthDate", record::getBirthDate)
                        .map(PersonDynamicSqlSupport.employed).toPropertyWhenPresent("employed", record::getEmployed)
                        .map(PersonDynamicSqlSupport.occupation).toPropertyWhenPresent("occupation", record::getOccupation)
                        .map(PersonDynamicSqlSupport.addressId).toPropertyWhenPresent("addressId", record::getAddressId)
        );
    }

    default Optional<PersonRecord> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, PersonDynamicSqlSupport.person, completer);
    }

    default List<PersonRecord> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, PersonDynamicSqlSupport.person, completer);
    }

    default List<PersonRecord> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, PersonDynamicSqlSupport.person, completer);
    }

    default Optional<PersonRecord> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
                c.where(PersonDynamicSqlSupport.id, isEqualTo(id_))
        );
    }

    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, PersonDynamicSqlSupport.person, completer);
    }

    static UpdateDSL<UpdateModel> updateAllColumns(PersonRecord record,
                                                   UpdateDSL<UpdateModel> dsl) {
        return dsl.set(PersonDynamicSqlSupport.id).equalTo(record::getId)
                .set(PersonDynamicSqlSupport.firstName).equalTo(record::getFirstName)
                .set(PersonDynamicSqlSupport.lastName).equalTo(record::getLastName)
                .set(PersonDynamicSqlSupport.birthDate).equalTo(record::getBirthDate)
                .set(PersonDynamicSqlSupport.employed).equalTo(record::getEmployed)
                .set(PersonDynamicSqlSupport.occupation).equalTo(record::getOccupation)
                .set(PersonDynamicSqlSupport.addressId).equalTo(record::getAddressId);
    }

    static UpdateDSL<UpdateModel> updateSelectiveColumns(PersonRecord record,
                                                         UpdateDSL<UpdateModel> dsl) {
        return dsl.set(PersonDynamicSqlSupport.id).equalToWhenPresent(record::getId)
                .set(PersonDynamicSqlSupport.firstName).equalToWhenPresent(record::getFirstName)
                .set(PersonDynamicSqlSupport.lastName).equalToWhenPresent(record::getLastName)
                .set(PersonDynamicSqlSupport.birthDate).equalToWhenPresent(record::getBirthDate)
                .set(PersonDynamicSqlSupport.employed).equalToWhenPresent(record::getEmployed)
                .set(PersonDynamicSqlSupport.occupation).equalToWhenPresent(record::getOccupation)
                .set(PersonDynamicSqlSupport.addressId).equalToWhenPresent(record::getAddressId);
    }

    default int updateByPrimaryKey(PersonRecord record) {
        return update(c ->
                c.set(PersonDynamicSqlSupport.firstName).equalTo(record::getFirstName)
                        .set(PersonDynamicSqlSupport.lastName).equalTo(record::getLastName)
                        .set(PersonDynamicSqlSupport.birthDate).equalTo(record::getBirthDate)
                        .set(PersonDynamicSqlSupport.employed).equalTo(record::getEmployed)
                        .set(PersonDynamicSqlSupport.occupation).equalTo(record::getOccupation)
                        .set(PersonDynamicSqlSupport.addressId).equalTo(record::getAddressId)
                        .where(PersonDynamicSqlSupport.id, isEqualTo(record::getId))
        );
    }

    default int updateByPrimaryKeySelective(PersonRecord record) {
        return update(c ->
                c.set(PersonDynamicSqlSupport.firstName).equalToWhenPresent(record::getFirstName)
                        .set(PersonDynamicSqlSupport.lastName).equalToWhenPresent(record::getLastName)
                        .set(PersonDynamicSqlSupport.birthDate).equalToWhenPresent(record::getBirthDate)
                        .set(PersonDynamicSqlSupport.employed).equalToWhenPresent(record::getEmployed)
                        .set(PersonDynamicSqlSupport.occupation).equalToWhenPresent(record::getOccupation)
                        .set(PersonDynamicSqlSupport.addressId).equalToWhenPresent(record::getAddressId)
                        .where(PersonDynamicSqlSupport.id, isEqualTo(record::getId))
        );
    }
}