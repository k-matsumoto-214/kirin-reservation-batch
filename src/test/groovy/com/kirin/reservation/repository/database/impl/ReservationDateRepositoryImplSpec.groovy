package com.kirin.reservation.repository.database.impl

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.kirin.reservation.model.ReservationTime
import com.kirin.reservation.repository.database.CsvDataSetLoader
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import spock.lang.Specification

import java.time.LocalDate

@MybatisTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners([
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
])
@Import(ReservationDateRepositoryImpl.class)
class ReservationDateRepositoryImplSpec extends Specification {

    @Autowired
    ReservationDateRepositoryImpl reservationDateRepository

    @DatabaseSetup(
            value = "/h2/data/reservationdaterepository/findByReservationDate/input",
            type = DatabaseOperation.CLEAN_INSERT
    )
    def "findByReservationDate_DBから取得できる"() {
        setup:
        def reservationDate = LocalDate.of(2022, 12, 15)
        def targetName = "尚大"
        def reservationTime = ReservationTime.PM

        when:
        def actual = reservationDateRepository.findByReservationDate(
                targetName, reservationDate, reservationTime)

        then:
        noExceptionThrown()
        actual.isEmpty() == false
        actual.getReservationTime() == ReservationTime.PM
        actual.getName() == targetName
        actual.getDate() == reservationDate
    }

    @DatabaseSetup(
            value = "/h2/data/reservationdaterepository/findByReservationDate/input",
            type = DatabaseOperation.CLEAN_INSERT
    )
    def "findByReservationDate_DBから取得できない"() {
        setup:
        def reservationDate = LocalDate.of(2099, 12, 15)
        def targetName = "尚大"
        def reservationTime = ReservationTime.PM

        when:
        def actual = reservationDateRepository.findByReservationDate(
                targetName, reservationDate, reservationTime)
        then:
        noExceptionThrown()
        actual.isEmpty() == true
    }
}
