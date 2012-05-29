package computation.algebra;

import computation.geom.GeomUtil;

public class Matrix {

	private static final boolean TRACE = false;
	private double _m[][];
	
	public Matrix(int rows, int columns){
		_m = new double[rows][columns];
	}
	
	public Matrix(double[] data, boolean row){
		this( row ? 1 : data.length, row ? data.length : 1 );
		for (int i = 0; i < data.length; i++) {
			set( row ? 0 : i, row ? i : 0, data[i] );
			
		}
	}

	
	public Matrix(int[] row){
		this(1,row.length);
		for (int i = 0; i < row.length; i++) {
			set(0,i,row[i]);
		}
	}
	
	public Matrix(double m[][] ){
		for (int i = 0; i < m.length-1; i++) {
			if( m[i].length != m[i+1].length ){
				throw new IllegalArgumentException();
			}
		}
		_m = m.clone();
		for (int i = 0; i < m.length; i++) {
			m[i] = m[i].clone();
		}
	}
	
	public int rows(){
		return _m.length;
	}
	
	public int columns(){
		return _m[0].length;
	}
	
	public double get(int row, int column){
		return _m[row][column];
	}
	
	public void set(int row, int column, double d){
		_m[row][column] = d;
	}
	
	public void swapColumns(int c1, int c2){
		for( int r = 0 ; r < rows() ; r++ ){
			double d = get(r,c1);
			set(r,c1, get(r,c2) );
			set( r, c2, d );
		}
	}

	public void swapRows(int r1, int r2){
		for( int c = 0 ; c < columns() ; c++ ){
			double d = get(r1,c);
			set(r1,c, get(r2,c) );
			set( r2, c, d );
		}
	}
	
	
	@Override
	public String toString() {
		String ret = "[";
		for( int r = 0 ; r < rows() ; r++ ){
			ret += "(";
			for ( int c = 0 ; c < columns() ; c++ ){
				ret += get(r,c);
				if( c < columns()-1 ){
					ret += ", ";
				}
			}
			ret += ")";
		}
		ret +="]";
		return ret;
	}

	public void sumRow(int rowSrc, double factor, int rowDst) {
		for( int c = 0 ; c < columns() ; c++ ){
			set( rowDst, c, get(rowDst,c) + get(rowSrc,c) * factor );
		}
	}

	private static void l(String s){
		if( TRACE ){
			System.out.println( s );
		}
	}
	
	public static void l(Matrix m){
		for( int row = 0 ; row < m.rows() ; row++ ){
			for( int col = 0 ; col < m.columns() ; col++ ){
				System.out.print( "\t" + m.get(row,col) );
			}
			System.out.println();
		}
	}
	
	
	public int[] triangularize(){
		
		Matrix columnIndex = new Matrix(1,columns());
		for (int c = 0; c < columns(); c++) {
			columnIndex.set(0,c,c);
		}
		
		int steps = Math.min(rows(), columns());
		for( int row = 0 ; row < steps ; row++ ){
			
			l( "Paso:" + row );
			if( TRACE ) l( this );
			
			// BUSCAR UNA FILA, COLUMNA HACIA ABAJO TAL QUE 
			// NO TENGA UN CERO
			int filaSinUnCero = -1;
			int columnaSinUnCero = -1;
			for( int c = row ; filaSinUnCero==-1 && c < steps; c++ ){
				for( int r = row ; filaSinUnCero==-1 && r < steps; r++ ){
					if( get(r, c) != 0 ){
						filaSinUnCero = r;
						columnaSinUnCero = c;
					}
				}
			}
			
			if( filaSinUnCero == -1 ){
				return null;
			}
		

			// PONER EL LA FILA row LA FILA QUE NO TENIA UN CERO
			l( "  fila,columna sin cero:" + filaSinUnCero + " " + columnaSinUnCero );
			swapRows(filaSinUnCero, row);
			swapColumns(columnaSinUnCero, row);
			columnIndex.swapColumns(columnaSinUnCero, row);
			if( TRACE ) l( this );
			
			// DEJAR EL RESTO DE LAS FILAS CON LA COLUMNA row A CERO
			for( int r = row+1 ; r < steps ; r++ ){
				double factor = get(r, row)/get(row, row);
				sumRow(row, -factor, r);
			}
			l( "  despues de poner a cero " );
			if( TRACE ) l( this );
		}
		
		// CALCULO EL RETORNO
		int[] ret = new int[columnIndex.columns()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (int)columnIndex.get(0, i);
			
		}
		return ret;
	}

	/**
	 *  / a b c \ /x\   /e\
	 *  | f g h | |y| + |i| = 0
	 *  \ j k l / \z/   \m/
	 * 
	 */
	public double[] solveEcuationSystem(){
		if( columns() != rows()+1 ){
			throw new IllegalArgumentException();
		}
		
		// TRIANGULARIZO Y MIRO QUE LOS TERMINOS INDEPENDIENTES NO SE HAN MOVIDO
		Matrix m = copy();
		int index[] = m.triangularize();
		if( index[rows()-1] != rows()-1 ){
			throw new IllegalStateException();
		}
		
		double variables[] = new double[columns()-1];
		for( int i = rows()-1 ; i >= 0 ; i-- ){
			double d = m.get(i, i);
			
			if( GeomUtil.isZero(d, GeomUtil.DELTA) ){
				// NO ES UN SISTEMA DETERMINADO (determinante es cero)
				return null;
			}
			
			// SUMA DE LAS VARIABLES * COEFICIENTES CONOCIDOS
			double suma = 0;
			for( int j = i+1 ; j < columns()-1; j++ ){
				suma += m.get(i,j)*variables[j];
			}
			suma += m.get(i,columns()-1);
			l( "  variable:" + i );
			l( "  coeficiente:" + d );
			l( "  suma de variable*coeficiente conocidos:" + suma);
			variables[i] = -suma/d;
			l( "    " + variables[i] );
		}
		
		// COLOCO LAS INCOGNITAS
		l( "  variables:" + new Matrix(variables, true).toString() );
		double ret[] = new double[variables.length];
		for( int i = 0 ; i < ret.length ; i++ ){
			ret[i] = variables[index[i]];
		}

		
		if( GeomUtil.PARANOID ){
			Matrix coeficents = extract(0, 0, m.rows()-1, m.rows()-1);
			Matrix varXcoefi = coeficents.multiply(new Matrix(variables, false));
			Matrix indep = extract( 0, m.columns()-1, m.rows()-1, m.columns()-1 );
			
			for( int row = 0 ; row < indep.rows() ; row ++ ){
				if( !GeomUtil.areEqual(varXcoefi.get(row, 0), -indep.get(row, 0), GeomUtil.DELTA) ){
					l(coeficents);
					l(varXcoefi);
					l(indep);
					throw new IllegalStateException();
				}
			}
			
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @return
	 */
	public Matrix copy(){
		Matrix ret = new Matrix(_m);
		return ret;
	}
	
	
	public Matrix multiply(Matrix m){
		if( columns() != m.rows() ){
			throw new IllegalArgumentException();
		}
		Matrix ret = new Matrix(rows(), m.columns());
		
		for(int row = 0 ; row < rows() ; row++){
			
			for( int col = 0 ; col < m.columns() ; col++ ){
				
				double d = 0;
				for( int i = 0 ; i < columns() ; i++ ){
					d += get(row,i) * m.get(i,col);
				}
				ret.set(row,col,d);
			}
		}
		return ret;
	}
	
	public Matrix extract( int rowIni, int columnIni, int rowEnd, int columnEnd ){
		if( rowIni < 0 || columnIni < 0 ){
			throw new IllegalArgumentException();
		}
		if( rowEnd >= rows() || columnEnd >= columns() ){
			throw new IllegalArgumentException();
		}
		if( rowIni > rowEnd || columnIni > columnEnd ){
			throw new IllegalArgumentException();
		}
		Matrix ret = new Matrix(rowEnd-rowIni+1, columnEnd-columnIni+1);
		for( int row = rowIni ; row <= rowEnd ; row++){
			for( int col = columnIni ; col <= columnEnd ; col++ ){
				ret.set( row-rowIni, col-columnIni, get( row, col));
			}
		}
		return ret;
	}
	
	
	public static void testSolve( Matrix m ){
		l("Sistema de ecuaciones");
		System.out.println(m);
		double variables[] = m.solveEcuationSystem();
		
		l("Variables");
		System.out.println( new Matrix(variables, false));

		
		Matrix coeficents = m.extract(0, 0, m.rows()-1, m.rows()-1);
		l("Coeficientes * variables");
		System.out.println( coeficents.multiply(new Matrix(variables, false)));
		
	}
	
	
	
	public static void main(String[] args) {
		double data[][] = {
				{1,2,3,4},
				{1,2,1,1},
				{1,1,3,2}
		};
		Matrix m = new Matrix(data);

		testSolve(m);
	}

}