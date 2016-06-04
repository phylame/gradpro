/*
 * Copyright 2014-2016 Peng Wan <phylame@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pw.phylame.linyin.client;

import javax.swing.*;
import java.text.MessageFormat;

/**
 * Created by Mnelx on 2016-4-13.
 */
public class GuiUtils {
    static void error(String msg, Object... args) {
        JOptionPane.showMessageDialog(LyClient.form, formatText(msg, args), "Ly Client", JOptionPane.ERROR_MESSAGE);
    }

    private static String formatText(String msg, Object... args) {
        return MessageFormat.format(msg, args);
    }
}
