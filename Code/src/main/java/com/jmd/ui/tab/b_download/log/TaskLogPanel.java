package com.jmd.ui.tab.b_download.log;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.service.InnerMqService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;

import java.awt.BorderLayout;
import java.io.Serial;

@Component
public class TaskLogPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -5274872683919500512L;

    private final InnerMqService innerMqService = InnerMqService.getInstance();
    private InnerMqClient client;

    private JTextArea logTextArea;

    @PostConstruct
    private void init() {

        this.setLayout(new BorderLayout(0, 0));

        this.logTextArea = new JTextArea();
        this.logTextArea.setEditable(false);
        this.logTextArea.setFocusable(false);
        this.logTextArea.setFont(StaticVar.FONT_YaHeiConsolas_13);
        this.logTextArea.setLineWrap(true);

        var scrollPane = new JScrollPane();
        scrollPane.setViewportView(this.logTextArea);
        this.add(scrollPane, BorderLayout.CENTER);

        try {
            this.subInnerMqMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    protected void destroy() {
        this.innerMqService.destroyClient(this.client);
    }

    private void subInnerMqMessage() throws Exception {
        this.client = this.innerMqService.createClient();
        this.client.<String>sub(Topic.DOWNLOAD_CONSOLE_LOG, (res) -> {
            SwingUtilities.invokeLater(() -> {
                logTextArea.append(res + "\n");
                // 自动滚动到底部
                logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
            });
        });
        this.client.<Boolean>sub(Topic.DOWNLOAD_CONSOLE_CLEAR, (res) -> {
            SwingUtilities.invokeLater(() -> logTextArea.setText(""));
        });
    }

}
