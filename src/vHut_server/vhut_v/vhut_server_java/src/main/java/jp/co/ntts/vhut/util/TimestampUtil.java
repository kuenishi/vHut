/**
 *
 */
package jp.co.ntts.vhut.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * @author NTT Software Corporation.
 */
public class TimestampUtil {
    private TimestampUtil() {
    }


    public static final int KILO = 1000;
    public static final int MEGA = KILO * 1000;
    public static final int GIGA = MEGA * 1000;

    private static final int SECVAL = 1000;
    private static final int MINVAL = 60 * SECVAL;
    private static final int HOURVAL = 60 * MINVAL;
    private static final int DAYVAL = 24 * HOURVAL;
    private static final int WEEKVAL = 7 * DAYVAL;

    private static Date currentDate = null;


    public static Date add(Date ts0, int num, TimestampUtil.Unit unit) {
        Date result = (Date) ts0.clone();
        result.setTime(ts0.getTime() + num * unit.getValue());
        return result;
    }

    //    public static int countDates(Timestamp begin, Timestamp end) {
    //        return count(begin, end, TimestampUtil.Unit.DAY);
    //    }
    //
    //    public static int count(Timestamp begin, Timestamp end, TimestampUtil.Unit unit) {
    //        if (begin.compareTo(end) <= 0) {
    //            return 0;
    //        }
    //
    //        return count((Date) begin, (Date) end, unit);
    //    }
    //
    //    public static int countDates(Date begin, Date end) {
    //        return count(begin, end, TimestampUtil.Unit.DAY);
    //    }
    //
    //    public static int count(Date begin, Date end, TimestampUtil.Unit unit) {
    //        if (begin.compareTo(end) <= 0) {
    //            return 0;
    //        }
    //
    //        long unitval = unit.getValue();
    //        //Timestamp値の正規化
    //        long nbgn = begin.getTime() / unitval;
    //        long nend = end.getTime() / unitval;
    //
    //        return (int) (nend - nbgn + 1);
    //    }

    //    public static Date normaliseAsBegin(Date val, TimestampUtil.Unit unit) {
    //        long unitval = unit.getValue();
    //        return new Date((val.getTime() / unitval) * unitval);
    //    }
    //
    //    public static Date normaliseAsEnd(Date val, TimestampUtil.Unit unit) {
    //        long timeval = val.getTime();
    //        long unitval = unit.getValue();
    //
    //        long result = timeval / unitval;
    //        if (result * unitval - timeval == 0) {
    //            result += 1;
    //        }
    //
    //        return new Date(result * unitval);
    //    }

    public static Timestamp add(Timestamp ts0, Date dt1) {
        return add(ts0, newTimestamp(dt1));
    }

    public static Timestamp add(Timestamp ts0, int num, TimestampUtil.Unit unit) {
        Timestamp result = (Timestamp) (ts0.clone());
        result.setTime(ts0.getTime() + num * unit.getValue());
        return result;
    }

    public static Timestamp add(Timestamp ts0, Timestamp ts1) {
        long millis = ts0.getTime() + ts1.getTime();
        int nanos = ts0.getNanos() + ts1.getNanos();

        int tmp = nanos - GIGA;
        if (tmp >= 0) {
            millis += KILO;
            nanos = tmp;
        }

        Timestamp result = new Timestamp(millis);
        result.setNanos(nanos);

        return result;
    }

    //    /**
    //     * @param order
    //     * @return
    //     * @throws DateIntegrityRutimeException
    //     */
    //    public static boolean checkDateIntegrity(Date begin, Date end) throws DateIntegrityRutimeException {
    //        if (begin == null) {
    //            throw new DateIntegrityRutimeException(DateIntegrityRutimeException.NULL_START);
    //        } else if (end == null) {
    //            throw new DateIntegrityRutimeException(DateIntegrityRutimeException.NULL_END);
    //        } else if (begin.after(end)) {
    //            throw new DateIntegrityRutimeException(DateIntegrityRutimeException.START_AFTER_END);
    //        } else if (begin.equals(end)) {
    //            throw new DateIntegrityRutimeException(DateIntegrityRutimeException.START_EQUALS_END);
    //        }
    //
    //        return true;
    //    }

    /**
     * @param order
     * @return
     * @throws DateIntegrityRutimeException
     */
    //    public static boolean checkNullableDateIntegrity(Date begin, Date end, Date defaultEnd) throws DateIntegrityRutimeException {
    //        if (begin == null) {
    //            begin = getCurrentDay();
    //        } else if (end == null) {
    //            end = defaultEnd;
    //        } else if (begin.after(end)) {
    //            throw new DateIntegrityRutimeException(DateIntegrityRutimeException.START_AFTER_END);
    //        } else if (begin.equals(end)) {
    //            throw new DateIntegrityRutimeException(DateIntegrityRutimeException.START_EQUALS_END);
    //        }
    //
    //        return true;
    //    }

    //    /**
    //     * @param order
    //     * @return
    //     */
    //    public static boolean checkDateIntegrity(OrderDto order) throws DateIntegrityRutimeException {
    //         return checkDateIntegrity(order.startTime, order.endTime);
    //    }

    /**
     * @return 今日(午前零時)のDate値
     */
    public static Date getCurrentDay() {
        /*
        GregorianCalendar result = new GregorianCalendar();
        result.set(Calendar.MILLISECOND, 0);
        result.set(Calendar.SECOND, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.HOUR, 0);
        return result.getTime();
        */
        return new Date(((new Date()).getTime() / TimestampUtil.DAYVAL) * TimestampUtil.DAYVAL);
    }

    /**
     * @return 現在時刻
     */
    public static Date getCurrentDate() {
        if (currentDate != null) {
            return currentDate;
        }
        return (new Date());
    }

    /**
     * 注意：テスト用に用意したメソッドです。メインコードからは使用しないように.
     * @param date 現在日時
     */
    public static void setCurrentDate(Date date) {
        currentDate = date;
    }

    /**
     * 現在時刻をjava.sql.Timestamp型で取得.
     * ユーティリティメソッド
     *
     * @return java.sql.Timestamp型の現在時刻
     */
    public static Timestamp getCurrentTimestamp() {
        return (new Timestamp((getCurrentDate()).getTime()));
    }

    /**
     * 現在[日付]（時間は00:00:000）をjava.sql.Timestamp型で取得.
     * ユーティリティメソッド
     *
     * @return java.sql.Timestamp型の現在時刻
     */
    public static Timestamp getCurrentDateAsTimestamp() {
        return floorAsDate(getCurrentTimestamp());
    }

    /**
     * Timestamp型からDate型を生成.
     * ユーティリティメソッド
     *
     * @param time java.sql.Timestamp 変換元
     * @return java.util.Date
     */
    public static Date newDate(Timestamp time) {
        return new Date(time.getTime());
    }

    /**
     * Date型からTimestamp型を生成.
     * ユーティリティメソッド
     *
     * @param date java.util.Date 変換元
     * @return java.sql.Timestamp
     */
    public static Timestamp newTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    public static Date subtract(Date dtl, Date dtr) {
        return (new Date(dtl.getTime() - dtr.getTime()));
    }

    public static Timestamp subtract(Timestamp ts0, Date dt1) {
        return subtract(ts0, newTimestamp(dt1));
    }

    public static Timestamp subtract(Timestamp ts0, int num, TimestampUtil.Unit unit) {
        Timestamp result = (Timestamp) (ts0.clone());
        result.setTime(ts0.getTime() - num * unit.getValue());
        return result;
    }

    public static Timestamp subtract(Timestamp ts0, Timestamp ts1) {
        long millis = ts0.getTime() - ts1.getTime();
        int nanos = ts0.getNanos() - ts1.getNanos();

        if (nanos < 0) {
            millis -= KILO;
            nanos += GIGA;
        }

        Timestamp result = new Timestamp(millis);
        result.setNanos(nanos);

        return result;
    }


    public enum Unit {
        MILLI, SEC, MIN, HOUR, DAY, WEEK;

        public long getValue() {
            switch (this) {
                case MILLI:
                    return 1;
                case SEC:
                    return SECVAL;
                case MIN:
                    return MINVAL;
                case HOUR:
                    return HOURVAL;
                case DAY:
                    return DAYVAL;
                case WEEK:
                    return WEEKVAL;
            }
            return 0;
        }
    }


    /**
     * 開始日時と終了日時から期間日数を求めます.　
     * 開始日と終了日を含めます。<br>
     * 日時設定にエラーがある場合は-1を返却します。
     *
     * @param startTime 開始日時
     * @param endTime 終了日時
     * @return 期間日数
     */
    public static final int countDate(Timestamp startTime, Timestamp endTime) {
        long sTime = startTime.getTime();
        long eTime = endTime.getTime();

        if (eTime < sTime) {
            return -1;
        }

        return (int) Math.floor(((double) (eTime - sTime) / DAYVAL)) + 1;
    }

    /**
     * 日付（時間は00:00:000）に切り捨てで変換する。
     * @param time 対象のタイムスタンプ
     * @return 切り捨てた
     */
    public static Timestamp floorAsDate(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 日付（時間は00:00:000）に切り上げで変換する。
     * @param time 対象のタイムスタンプ
     * @return 切り捨てた
     */
    public static Timestamp ceilAsDate(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time.getTime() + DAYVAL);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }
}


/**
 * =====================================================================
 * 
 *    Copyright 2011 NTT Sofware Corporation
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * =====================================================================
 */
