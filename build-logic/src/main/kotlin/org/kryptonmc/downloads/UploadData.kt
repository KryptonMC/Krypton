package org.kryptonmc.downloads

class UploadData(val projectId: String, val version: String, val changes: List<Change>) {

    class Change(val commit: String, val summary: String, val message: String)
}
