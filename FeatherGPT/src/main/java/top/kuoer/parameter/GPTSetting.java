package top.kuoer.parameter;

import java.util.List;

public class GPTSetting {

    private transient String apiUrl;
    private transient String key;

    private String model;   //要使用的模型的 ID。有关哪些模型适用于聊天 API 的详细信息，请参阅模型端点兼容性表。
    private List<Message> messages;    //以聊天格式生成聊天完成的消息。
    private Double temperature;    //使用什么采样温度，介于 0 和 2 之间。较高的值（如 0.8）将使输出更加随机，而较低的值（如 0.2）将使输出更加集中和确定。 我们通常建议改变这个或top_p但不是两者。
    private Integer top_p;  //一种替代温度采样的方法，称为核采样，其中模型考虑具有 top_p 概率质量的标记的结果。所以 0.1 意味着只考虑构成前 10% 概率质量的标记。 我们通常建议改变这个或temperature但不是两者。
    private Integer n;  //为每个输入消息生成多少个聊天完成选项。
    private Boolean stream; //如果设置，将发送部分消息增量，就像在 ChatGPT 中一样。当令牌可用时，令牌将作为纯数据服务器发送事件data: [DONE]发送，流由消息终止。
    private String stop;    //API 将停止生成更多令牌的最多 4 个序列。
    private Integer max_tokens; //聊天完成时生成的最大令牌数。 输入标记和生成标记的总长度受模型上下文长度的限制。
    private Short presence_penalty; //-2.0 和 2.0 之间的数字。正值会根据到目前为止是否出现在文本中来惩罚新标记，从而增加模型谈论新主题的可能性。 查看有关频率和存在惩罚的更多信息。
    private Short frequency_penalty;    //-2.0 和 2.0 之间的数字。正值会根据新标记在文本中的现有频率对其进行惩罚，从而降低模型逐字重复同一行的可能性。 查看有关频率和存在惩罚的更多信息。
    private String user;    //代表您的最终用户的唯一标识符，可以帮助 OpenAI 监控和检测滥用行为。了解更多。

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getTop_p() {
        return top_p;
    }

    public void setTop_p(Integer top_p) {
        this.top_p = top_p;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public Integer getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(Integer max_tokens) {
        this.max_tokens = max_tokens;
    }

    public Short getPresence_penalty() {
        return presence_penalty;
    }

    public void setPresence_penalty(Short presence_penalty) {
        this.presence_penalty = presence_penalty;
    }

    public Short getFrequency_penalty() {
        return frequency_penalty;
    }

    public void setFrequency_penalty(Short frequency_penalty) {
        this.frequency_penalty = frequency_penalty;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
