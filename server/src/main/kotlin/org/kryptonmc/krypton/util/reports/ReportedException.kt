package org.kryptonmc.krypton.util.reports

import java.lang.RuntimeException

class ReportedException(val report: CrashReport) : RuntimeException() {

    override val cause = report.exception
    override val message = report.title
}
