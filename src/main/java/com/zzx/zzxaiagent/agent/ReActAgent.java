package com.zzx.zzxaiagent.agent;

import com.zzx.zzxaiagent.agent.model.AgentState;

public abstract class ReActAgent extends BaseAgent {

    /**
     * 定义思考，决定下一步是否要进行
     * true 表示要执行
     * false 表示不需要执行
     *
     * @return 是否要执行
     */
    public abstract boolean think();


    /**
     * 执行思考
     *
     * @return 执行的结果
     */
    public abstract String act();


    /**
     * 执行单个步骤： 思考 - 行动
     *
     * @return 步骤执行的结果
     */
    @Override
    public String step() {
        try {
            boolean shouldAct = this.think();
            if (!shouldAct) {
                // 不需要执行
                super.setState(AgentState.FINISHED);
                return "思考完成 - 无需行动";
            }
            String result = this.act();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "执行步骤失败：" + e.getMessage();
        }
    }
}
