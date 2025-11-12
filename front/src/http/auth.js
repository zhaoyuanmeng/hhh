/*
 * @Author: your name
 * @Date: 2021-08-02 00:30:53
 * @LastEditTime: 2024-06-19 11:39:12
 * @LastEditors: Alex
 * @Description: In User Settings Edit
 * @FilePath: \aircityinit\src\utils\auth.js
 */
import Cookies from "js-cookie";

const TokenKey = "token";

const getToken = () => {
	return Cookies.get(TokenKey);
};

const setToken = (token) => {
	return Cookies.set(TokenKey, token);
};

const removeToken = () => {
	return Cookies.remove(TokenKey);
};
export {getToken, setToken, removeToken};
