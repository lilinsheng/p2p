package cn.wolfcode.p2p.util;

/**
 * 用户状态类，记录用户在平台使用系统中所有的状态。
 * 
 * @author Administrator
 */
public class BitStatesUtils {

	/**
	 * 预先给定的用户的各种功能状态的值 
	 */
	public final static Long OP_BIND_PHONE = 1L << 0; // 用户绑定手机状态码	1  = ...000001
	public final static Long OP_BIND_EMAIL = 1L << 1; // 用户绑定邮箱状态码	2  = ...000010
	public final static Long OP_BASIC_INFO = 1L << 2; // 用户填写基本资料		4  = ...000100
	public final static Long OP_REAL_AUTH = 1L << 3; // 用户完成实名认证		8
	public final static Long OP_VEDIO_AUTH = 1L << 4; // 用户完成视频认证		16  =  1 * 2 * 2 * 2 * 2
	public final static Long HAS_BIDREQUEST_IN_PROCESS = 1L << 5;// 用户有一个借款在申请流程当中	32
	public final static Long HAS_GET_EXPGOLD = 1L << 6;// 用户有领取体验金	64

	/**
     *
	 * 判断 states 中是否拥有 value 这个状态值
	 * @param states
	 *            所有状态值
	 * @param value
	 *            需要判断状态值
	 * @return 是否存在
	 */
	public static boolean hasState(long states, long value) {
		return (states & value) != 0;
	}

	/**

	 * 给 states 中添加 value 这个状态值
	 * @param states
	 *            已有状态值
	 * @param value
	 *            需要添加状态值
	 * @return 新的状态值
	 */
	public static long addState(long states, long value) {
		if (hasState(states, value)) {
			return states;
		}
		return (states | value);
	}

	/**
	 * @param states
	 *            已有状态值
	 * @param value
	 *            需要删除状态值
	 * @return 新的状态值
	 */
	public static long removeState(long states, long value) {
		if (!hasState(states, value)) {
			return states;
		}
		return states ^ value;
	}
	public static void main(String[] a){
		System.out.println(removeState(63,32));
	}
}
