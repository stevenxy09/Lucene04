package cn.bts.lucene;

import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
* @author stevenxy E-mail:random_xy@163.com
* @Date 2018年6月14日
* @Description 
*/
public class SearchTest {
	
	private Directory dir;
	private IndexReader reader;
	private IndexSearcher is;
	
	@Before
	public void setUp() throws Exception {
		dir=FSDirectory.open(Paths.get("D:\\lucene5"));
		reader=DirectoryReader.open(dir);
		is=new IndexSearcher(reader);
	}

	@After
	public void tearDown() throws Exception {
		reader.close();
	}
	
	/**
	 * 对指项范围查询
	 */
	@Test
	public void testTarmRangeQuery()throws Exception {
		
		TermRangeQuery query=new TermRangeQuery("desc", new BytesRef("a".getBytes()), new BytesRef("b".getBytes()), true, true);
		TopDocs hits=is.search(query, 10);
		for(ScoreDoc scoreDocs:hits.scoreDocs) { 
			Document document=is.doc(scoreDocs.doc);
			System.out.println(document.get("id"));
			System.out.println(document.get("city"));
			System.out.println(document.get("desc"));
		}
	}
	
	/**
	 * 指定数字范围查询
	 * @throws Exception
	 */
	@Test
	public void testNumericRangeQuery()throws Exception{
		NumericRangeQuery<Integer> query=NumericRangeQuery.newIntRange("id", 1, 2, true, true);
		TopDocs hits=is.search(query, 10);
		for(ScoreDoc scoreDocs:hits.scoreDocs) { 
			Document document=is.doc(scoreDocs.doc);
			System.out.println(document.get("id"));
			System.out.println(document.get("city"));
			System.out.println(document.get("desc"));
		}
	}
	
	/**
	 * 指定字符串开头搜索
	 * @throws Exception
	 */
	@Test
	public void testPrefixQuery()throws Exception{
		PrefixQuery query=new PrefixQuery(new Term("city","a"));
		TopDocs hits=is.search(query, 10);
		for(ScoreDoc scoreDocs:hits.scoreDocs) { 
			Document document=is.doc(scoreDocs.doc);
			System.out.println(document.get("id"));
			System.out.println(document.get("city"));
			System.out.println(document.get("desc"));
		}
		
	}
	
	/**
	 * 多条件查询
	 * @throws Exception
	 */
	@Test
	public void testBooleanQuery()throws Exception{
		NumericRangeQuery<Integer> query1=NumericRangeQuery.newIntRange("id", 1, 2, true, true);
		PrefixQuery query2=new PrefixQuery(new Term("city","a"));
		BooleanQuery.Builder booleanQuery=new BooleanQuery.Builder();
		booleanQuery.add(query1,BooleanClause.Occur.MUST);
		booleanQuery.add(query2,BooleanClause.Occur.MUST);
		TopDocs hits=is.search(booleanQuery.build(), 10);
		for(ScoreDoc scoreDocs:hits.scoreDocs) { 
			Document document=is.doc(scoreDocs.doc);
			System.out.println(document.get("id"));
			System.out.println(document.get("city"));
			System.out.println(document.get("desc"));
		}
		
	}
	
}
