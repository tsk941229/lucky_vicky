const login = async () => {

    const memberId = getDom("memberId").value;
    const password = getDom("password").value;

    console.log("memberId", memberId);
    console.log("password", password);

    const param = {
        memberId: memberId,
        password: password
    }

    await fetchPOST("/api/admin/auth/login", param);

}
