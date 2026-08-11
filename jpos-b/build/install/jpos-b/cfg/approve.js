function prepare(id, context) {
    var request = context.get("REQUEST");

    if (request == null) {
        context.log("APPROVE: REQUEST is null");
        return 192;
    }

    context.log("APPROVE: received MTI=" + request.getMTI());

    var response = request.clone();

    response.setResponseMTI();
    response.set(39, "00");

    context.put("RESPONSE", response);

    context.log("APPROVE: response MTI=" + response.getMTI() + " DE39=" + response.getString(39));

    return 1;
}