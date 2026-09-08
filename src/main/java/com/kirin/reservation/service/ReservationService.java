package com.kirin.reservation.service;

import com.kirin.reservation.config.KirinWebConfig;
import com.kirin.reservation.config.TimeConfig;
import com.kirin.reservation.config.WebDriverConfig;
import com.kirin.reservation.model.ReservationDate;
import com.kirin.reservation.model.ReservationTime;
import com.kirin.reservation.repository.database.ReservationDateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationDateRepository reservationDateRepository;

	private final WebDriverConfig webDriverConfig;

	private final TimeConfig timeConfig;

	private final KirinWebConfig webConfig;


	/**
	 * DBから予約情報を取得する
	 *
	 * @param targetName      予約対象者名
	 * @param targetDate      予約対象日付
	 * @param reservationTime 予約対象時間帯
	 * @return 予約情報
	 */
	@Retryable
	public ReservationDate findReservationTarget(String targetName, LocalDate targetDate, ReservationTime reservationTime) {

		// DBから予約情報を取得
		return reservationDateRepository.findByReservationDate(targetName, targetDate, reservationTime);
	}

	/**
	 * web予約をおこなってその結果(受付番号)を返す
	 *
	 * @param targetName      予約対象者名
	 * @param reservationTime 予約時間帯
	 * @param clock
	 * @return 予約成功のとき受付番号
	 */
	public int reserve(String targetName, ReservationTime reservationTime, Clock clock) {
		WebDriver webDriver = webDriverConfig.getWebDriver();

		try {
			log.info("{}の予約を開始", targetName);

			webDriver.get(webConfig.url());

			// email入力
			webDriver.findElement(webConfig.emailSelector()).sendKeys(webConfig.user());

			// password入力
			webDriver.findElement(webConfig.passwordSelector()).sendKeys(webConfig.password());

			// ログイン実行
			webDriver.findElement(webConfig.loginSelector()).click();

			log.info("ログイン");

			// 予約開始時刻を取得
			LocalDateTime targetTime = timeConfig.getTargetTime(reservationTime, clock);

			// 予約開始時間まで待機
//			timeConfig.until(targetTime, clock);

			log.info("予約開始");

			// 予約開始時間になったら予約画面を開く
			webDriver.get(webConfig.reservationUrl(reservationTime, clock));

			WebElement actualInput = webDriver.findElement(webConfig.userIdSelector());

			boolean isChecked = actualInput.isSelected();

			if (isChecked) {
				log.info("【判定】すでにチェック済みです");
			} else {
				log.info("【判定】チェックされていないので対象者をチェックします");

				JavascriptExecutor js = (JavascriptExecutor) webDriver;

				String forceCheckScript =
						"arguments[0].checked = true;" +
								"arguments[0].dispatchEvent(new Event('change', { bubbles: true }));";

				js.executeScript(forceCheckScript, actualInput);

				if (actualInput.isSelected()) {
					log.info("【確認結果】成功：チェックボックス（ID: " + actualInput.getAttribute("id") + "）が [ON] になりました！");
				} else {
					log.error("【確認結果】失敗：これでもONにならない場合は要素が見つかっていません");
				}
			}

			log.info("予約ボタンをクリック");


			// 予約実行
			JavascriptExecutor js = (JavascriptExecutor) webDriver;
			js.executeScript("window.confirm = function(msg) { return true; };");
			webDriver.findElement(webConfig.executeSelector()).click();

			log.info("予約完了");

			// 予約受付番号を取得する
			final String reservationOrderString = webDriver.findElement(webConfig.reservationOrderSelector()).getText();

			log.info("予約情報取得完了");

			return Integer.parseInt(reservationOrderString);

		} catch (Exception e) {
			log.error("{}の予約中にエラー発生 原因: {}", targetName, e.toString());
			throw new RuntimeException(e);

		} finally {
			webDriver.quit();
		}
	}
}
