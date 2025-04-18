const login = async () => {

    const memberId = getDom("memberId").value;
    const password = getDom("password").value;

    const param = {
        memberId: memberId,
        password: password
    }

    const {status} = await fetchPOST("/api/admin/auth/login", param);

}
