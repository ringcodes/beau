/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.beau.utils;

import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * markdown解析
 *
 * @author liushilin
 * @date 2021/12/15
 */
public class CodeNodeRenderer implements NodeRenderer {
    private final HtmlWriter html;
    private final Map<String, String> attr = new HashMap<String, String>() {
        {
            put("class", "mermaid");
        }
    };

    CodeNodeRenderer(HtmlNodeRendererContext context) {
        this.html = context.getWriter();
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        // Return the node types we want to use this renderer for.
        return Collections.singleton(FencedCodeBlock.class);
    }

    @Override
    public void render(Node node) {
        // We only handle one type as per getNodeTypes, so we can just cast it here.
        FencedCodeBlock codeBlock = (FencedCodeBlock) node;
        html.line();
        if ("mermaid".equals(codeBlock.getInfo())) {
            html.tag("div", attr);
            html.text(codeBlock.getLiteral());
            html.tag("/div");
        } else {
            html.tag("pre");
            html.tag("code");
            html.text(codeBlock.getLiteral());
            html.tag("/code");
            html.tag("/pre");
        }
        html.line();
    }
}
