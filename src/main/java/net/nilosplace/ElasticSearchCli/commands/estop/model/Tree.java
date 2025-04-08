package net.nilosplace.ElasticSearchCli.commands.estop.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;

@Data
public class Tree<T> {

	private Node<T> root;

	public Tree(T rootData) {
		root = new Node<>(rootData);
	}

	public static class Node<T> {
		@Getter private T data;
		// private Node<T> parent;
		@Getter private List<Node<T>> children = new ArrayList<>();

		public Node(T data) {
			this.data = data;
		}

		public void addChild(Node<T> node) {
			children.add(node);
		}
	}

}
