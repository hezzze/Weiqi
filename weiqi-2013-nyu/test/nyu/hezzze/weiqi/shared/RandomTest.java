/*
package nyu.hezzze.weiqi.shared;

import static nyu.hezzze.weiqi.shared.Gamer.BLACK;
import static nyu.hezzze.weiqi.shared.Gamer.O;
import static nyu.hezzze.weiqi.shared.Gamer.X;
import static nyu.hezzze.weiqi.shared.Gamer._;
import static org.junit.Assert.*;

import org.junit.Test;

public class RandomTest {

	@Test
	public void testFormingGroups() {
		Gamer[][] board = {
				//0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18
				{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 0
				{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 1
				{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 2
				{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 3
				{ O, O, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 4
				{ X, X, O, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 5
				{ _, X, O, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 6
				{ X, X, X, O, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 7
				{ O, X, X, O, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 8
				{ _, O, O, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 9
				{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 10
				{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 11
				{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 12
				{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 13
				{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 14 
				{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 15
				{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 16
				{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }, // 17
				{ _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ }  // 18
		};
		State newState = new State(board, BLACK, false, null);
		assertEquals(newState.getGroups().size(), 8);

		for (Group group : newState.getGroups()) {
			System.out.println(group);
			if (group.members.size() == 8) {
				System.out.println(group.getGroupPerimeterPositions());
			}
		}

		assertEquals(newState.getEmptyGroups().size(), 2);

		for (Group group : newState.getGroups()) {
			System.out.println(group);
		}
				
		
	}
	
	
	@Test
	public void testJudge() {
		Gamer[][] before = {
				//0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18
				{ _, _, _, O, O, X, _, _, _, _, X, O, O, O, O, _, _, _, _ }, // 0
				{ _, _, _, O, X, X, X, _, _, _, _, X, X, X, O, _, O, _, O }, // 1
				{ _, _, _, O, X, X, X, X, X, _, X, _, X, O, _, _, O, O, X }, // 2
				{ _, _, O, _, O, X, O, O, X, _, X, _, X, O, O, O, X, X, X }, // 3
				{ _, _, O, O, O, O, O, O, O, X, X, X, O, O, O, X, _, X, _ }, // 4
				{ _, O, X, X, X, O, O, X, X, O, X, X, X, O, O, X, X, _, _ }, // 5
				{ _, _, O, X, X, X, X, X, O, O, O, X, X, O, O, X, _, _, _ }, // 6
				{ _, _, O, O, X, _, _, X, O, O, X, O, O, O, X, X, O, X, _ }, // 7
				{ _, O, X, X, _, X, X, X, O, O, X, X, X, X, X, O, X, X, _ }, // 8
				{ _, O, X, X, X, X, O, O, X, O, X, O, X, O, X, O, O, _, _ }, // 9
				{ _, O, X, O, O, O, _, _, X, O, X, O, O, O, X, _, _, _, _ }, // 10
				{ O, O, O, _, _, O, O, O, O, O, X, O, _, O, O, X, _, _, _ }, // 11
				{ X, X, O, O, O, X, X, X, O, X, O, O, O, X, X, X, _, _, _ }, // 12
				{ X, _, X, O, X, X, _, X, O, _, X, _, O, X, _, _, _, _, _ }, // 13
				{ _, X, X, O, X, _, X, O, O, _, _, _, _, O, X, X, X, _, _ }, // 14 
				{ _, O, X, X, _, X, O, O, O, _, _, _, _, O, O, O, X, X, X }, // 15
				{ _, X, _, _, _, X, X, O, _, _, _, _, _, _, _, _, O, O, X }, // 16
				{ _, _, _, O, X, _, X, X, O, O, _, _, _, _, _, _, O, _, O }, // 17
				{ _, _, _, _, _, _, _, X, X, O, _, _, _, _, _, _, _, _, _ }  // 18
		};
		
		Gamer[][] expected = {
				//0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18
				{ _, _, _, O, O, X, _, _, _, _, X, O, O, O, O, _, _, _, _ }, // 0
				{ _, _, _, O, X, X, X, _, _, _, _, X, X, X, O, _, O, _, O }, // 1
				{ _, _, _, O, X, X, X, X, X, _, X, _, X, O, _, _, O, O, X }, // 2
				{ _, _, O, _, O, X, O, O, X, _, X, _, X, O, O, O, X, X, X }, // 3
				{ _, _, O, O, O, O, O, O, O, X, X, X, O, O, O, X, _, X, _ }, // 4
				{ _, O, X, X, X, O, O, X, X, O, X, X, X, O, O, X, X, _, _ }, // 5
				{ _, _, O, X, X, X, X, X, O, O, O, X, X, O, O, X, _, _, _ }, // 6
				{ _, _, O, O, X, _, _, X, O, O, X, O, O, O, X, X, _, X, _ }, // 7
				{ _, O, X, X, _, X, X, X, O, O, X, X, X, X, X, _, X, X, _ }, // 8
				{ _, O, X, X, X, X, O, O, _, O, X, O, X, O, X, _, _, _, _ }, // 9
				{ _, O, X, O, O, O, _, _, _, O, X, O, O, O, X, _, _, _, _ }, // 10
				{ O, O, O, _, _, O, O, O, O, O, X, O, _, O, O, X, _, _, _ }, // 11
				{ X, X, O, O, O, X, X, X, O, _, O, O, O, X, X, X, _, _, _ }, // 12
				{ X, _, X, O, X, X, _, X, O, _, _, _, O, X, _, _, _, _, _ }, // 13
				{ _, X, X, O, X, _, X, O, O, _, _, _, _, O, X, X, X, _, _ }, // 14 
				{ _, _, X, X, _, X, O, O, O, _, _, _, _, O, O, O, X, X, X }, // 15
				{ _, X, _, _, _, X, X, O, _, _, _, _, _, _, _, _, O, O, X }, // 16
				{ _, _, _, _, X, _, X, X, O, O, _, _, _, _, _, _, O, _, O }, // 17
				{ _, _, _, _, _, _, _, X, X, O, _, _, _, _, _, _, _, _, _ }  // 18
		};
		
		State endState = new State(before, BLACK, false, null);
		Judge judge = new Judge(endState);

		assertArrayEquals(judge.getFinalBoard(), expected);
		assertEquals(judge.getPointsOfBlack(), 182);
		
		
	}

}

*/