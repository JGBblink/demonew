package com.example.demo_test.service;

import java.util.ArrayList;
import java.util.List;

public class TreeDemo {
	private String data;
	private TreeDemo left;
	private TreeDemo right;

	public TreeDemo(String data, TreeDemo left, TreeDemo right) {
		this.data = data;
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return data;
	}

	public static void main(String[] args) {

		String input = "1 2 3 # # 6 7 # # # # # # # 8";
		// 构造二叉树
		String[] strs = new String[9];
		strs[0] = "1";
		strs[1] = "2";
		strs[2] = "3";
		strs[3] = "#";
		strs[4] = "#";
		strs[5] = "6";
		strs[6] = "7";
		strs[7] = "#";
		strs[8] = "#";

		buildTree(strs);
	}


	public static TreeDemo buildTree(String[] nodes) {
		List<TreeDemo> nodeList = new ArrayList<>(nodes.length);
		for(int i = 0; i < nodes.length; i++) {
			nodeList.add(new TreeDemo(nodes[i],null,null));
		}

		for(int i = 0; i < nodes.length/2 - 1; i++) {
			nodeList.get(i).left = nodeList.get(i*2 + 1);
			nodeList.get(i).right = nodeList.get(i*2 + 2);
		}

		System.out.println(nodeList);


		return null;
	}

	public void print(TreeDemo tree) {
		System.out.println(tree);
		if(tree.left != null) {
			print(tree.left);
		}
		if(tree.right != null) {
			print(tree.right);
		}
	}


}

