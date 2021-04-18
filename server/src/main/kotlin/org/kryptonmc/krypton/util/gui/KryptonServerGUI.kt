package org.kryptonmc.krypton.util.gui

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.concurrent.DefaultUncaughtExceptionHandler
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.util.QueueLogAppender
import org.kryptonmc.krypton.util.gui.stats.GuiStatsComponent
import java.awt.*
import java.awt.event.*
import java.util.concurrent.atomic.*
import javax.swing.*
import javax.swing.ScrollPaneConstants.*
import javax.swing.border.EtchedBorder
import javax.swing.border.TitledBorder

class KryptonServerGUI(private val server: KryptonServer) : JComponent(), AutoCloseable {

    val finalizers = mutableListOf<Runnable>()
    private val isClosing = AtomicBoolean()

    private lateinit var logAppenderThread: Thread

    init {
        preferredSize = Dimension(854, 480)
        layout = BorderLayout()

        try {
            add(buildChatPanel(), "Center")
            add(buildInfoPanel(), "West")
        } catch (exception: Exception) {
            LOGGER.error("Could not build server GUI", exception)
        }
    }

    fun start() = logAppenderThread.start()

    private fun buildInfoPanel(): JComponent {
        val stats = GuiStatsComponent(server)
        finalizers.add(stats::close)

        return JPanel(BorderLayout()).apply {
            add(stats, "North")
            add(buildPlayerPanel(), "Center")
            border = TitledBorder(EtchedBorder(), "Stats")
        }
    }

    private fun buildPlayerPanel(): JComponent {
        val playerList = PlayerListComponent(server)
        return JScrollPane(playerList, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_AS_NEEDED).apply {
            border = TitledBorder(EtchedBorder(), "Players")
        }
    }

    private fun buildChatPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        val textArea = ANSITextPane().apply {
            isEditable = false
            font = MONOSPACED
            addFocusListener(object : FocusAdapter() {
                override fun focusGained(e: FocusEvent?) = Unit
            })
        }
        val scrollPane = JScrollPane(textArea, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_AS_NEEDED)

        val textField = JTextField().apply {
            addActionListener {
                text.trim().takeIf { it.isNotEmpty() }?.let { server.commandManager.dispatch(server.console, it) }
                text = ""
            }
        }

        panel.add(scrollPane, "Center")
        panel.add(textField, "South")
        panel.border = TitledBorder(EtchedBorder(), "Log and chat")

        logAppenderThread = Thread {
            do {
                val nextLogEvent = QueueLogAppender.nextLogEvent("ServerGuiConsole") ?: break
                print(textArea, scrollPane, nextLogEvent)
            } while (true)
        }.apply {
            uncaughtExceptionHandler = DefaultUncaughtExceptionHandler(LOGGER)
            isDaemon = true
        }
        return panel
    }

    private fun print(textPane: ANSITextPane, scrollPane: JScrollPane, text: String) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater { print(textPane, scrollPane, text) }
            return
        }

//        val document = textArea.document
        val scrollBar = scrollPane.verticalScrollBar

        val flag = if (scrollPane.viewport.view == textPane) {
            scrollBar.value + scrollBar.size.height + MONOSPACED.size * 4 > scrollBar.maximum
        } else false

        textPane.appendANSI(text)
//        document.insertString(document.length, text, null)
        if (flag) scrollBar.value = Integer.MAX_VALUE
    }

    override fun close() {
        if (!isClosing.getAndSet(true)) finalizers.forEach(Runnable::run)
    }

    companion object {

        private val MONOSPACED = Font("Monospaced", 0, 12)
        private val LOGGER = logger<KryptonServerGUI>()

        fun open(server: KryptonServer): KryptonServerGUI {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

            val gui = KryptonServerGUI(server)
            val frame = JFrame("Krypton").apply {
                defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
                add(gui)
                pack()
                setLocationRelativeTo(null)
                isVisible = true
                addWindowListener(object : WindowAdapter() {
                    override fun windowClosing(event: WindowEvent) {
                        if (gui.isClosing.getAndSet(true)) return
                        title = "Krypton - shutting down..."
                        server.stop()
                        gui.finalizers.forEach(Runnable::run)
                    }
                })
            }
            gui.finalizers.add(frame::dispose)
            gui.start()
            return gui
        }
    }
}