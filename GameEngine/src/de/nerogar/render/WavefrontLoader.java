package de.nerogar.render;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WavefrontLoader {

	private static HashMap<String, WavefrontMesh> meshMap = new HashMap<String, WavefrontMesh>();

	private static ArrayList<float[]> vertices = new ArrayList<float[]>();
	private static ArrayList<float[]> normals = new ArrayList<float[]>();
	private static ArrayList<float[]> texCoords = new ArrayList<float[]>();

	private static ArrayList<int[]> faceVertsTriangles = new ArrayList<int[]>();
	private static ArrayList<int[]> faceTexTriangles = new ArrayList<int[]>();
	private static ArrayList<int[]> faceNormalsTriangles = new ArrayList<int[]>();

	private static float[] verticesTriangles, normalsTriangles, texCoordsTriangles;
	private static WavefrontMesh object;

	public static WavefrontMesh loadObject(String filename) {
		object = meshMap.get(filename);
		if (object != null) return object;
		else object = new WavefrontMesh();

		clearLists();

		BufferedReader reader;

		try {

			reader = new BufferedReader(new FileReader(filename));

			String line;
			while ((line = reader.readLine()) != null) {

				String[] lineSplit = line.split(" ");

				switch (lineSplit[0]) {
				case "v":
					if (lineSplit.length == 4) {
						float f1 = Float.valueOf(lineSplit[1]);
						float f2 = Float.valueOf(lineSplit[2]);
						float f3 = Float.valueOf(lineSplit[3]);

						addVertex(f1, f2, f3);
					}
					break;
				case "vt":
					if (lineSplit.length == 3) {
						float f1 = Float.valueOf(lineSplit[1]);
						float f2 = 1f - Float.valueOf(lineSplit[2]);

						addTexCoord(f1, f2);
					}

					break;
				case "vn":
					if (lineSplit.length == 4) {
						float f1 = Float.valueOf(lineSplit[1]);
						float f2 = Float.valueOf(lineSplit[2]);
						float f3 = Float.valueOf(lineSplit[3]);

						addNormal(f1, f2, f3);
					}
					break;
				case "f":
					String lineData = line.substring(2);
					lineSplit = lineData.split(" ");

					if (lineSplit.length == 3) {
						int f1 = Integer.valueOf(lineSplit[0].split("/")[0]);
						int f2 = Integer.valueOf(lineSplit[1].split("/")[0]);
						int f3 = Integer.valueOf(lineSplit[2].split("/")[0]);
						int t1 = 0, t2 = 0, t3 = 0;
						int n1 = 0, n2 = 0, n3 = 0;

						if (lineSplit[0].split("/").length > 1) {
							t1 = Integer.valueOf(lineSplit[0].split("/")[1]);
							t2 = Integer.valueOf(lineSplit[1].split("/")[1]);
							t3 = Integer.valueOf(lineSplit[2].split("/")[1]);

							if (lineSplit[0].split("/").length > 2) {
								n1 = Integer.valueOf(lineSplit[0].split("/")[2]);
								n2 = Integer.valueOf(lineSplit[1].split("/")[2]);
								n3 = Integer.valueOf(lineSplit[2].split("/")[2]);

							}
						}
						addFaceVerts(new int[] { f1, f2, f3 });
						addFaceTex(new int[] { t1, t2, t3 });
						addFaceNormal(new int[] { n1, n2, n3 });

					}

					break;
				}

			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		initObject();

		meshMap.put(filename, object);

		return object;
	}

	private static void clearLists() {
		vertices.clear();
		normals.clear();
		texCoords.clear();

		faceVertsTriangles.clear();
		faceTexTriangles.clear();
		faceNormalsTriangles.clear();
	}

	private static void initObject() {
		verticesTriangles = new float[faceVertsTriangles.size() * 3 * 3];
		normalsTriangles = new float[faceVertsTriangles.size() * 3 * 3];
		texCoordsTriangles = new float[faceVertsTriangles.size() * 3 * 2];

		for (int i = 0; i < faceVertsTriangles.size(); i++) {
			int[] vert = faceVertsTriangles.get(i);
			int[] tex = faceTexTriangles.get(i);
			int[] norm = faceNormalsTriangles.get(i);

			verticesTriangles[i * 9 + 0] = vertices.get(vert[0] - 1)[0];
			verticesTriangles[i * 9 + 1] = vertices.get(vert[0] - 1)[1];
			verticesTriangles[i * 9 + 2] = vertices.get(vert[0] - 1)[2];
			texCoordsTriangles[i * 6 + 0] = texCoords.get(tex[0] - 1)[0];
			texCoordsTriangles[i * 6 + 1] = texCoords.get(tex[0] - 1)[1];
			normalsTriangles[i * 9 + 0] = normals.get(norm[0] - 1)[0];
			normalsTriangles[i * 9 + 1] = normals.get(norm[0] - 1)[1];
			normalsTriangles[i * 9 + 2] = normals.get(norm[0] - 1)[2];

			verticesTriangles[i * 9 + 3] = vertices.get(vert[1] - 1)[0];
			verticesTriangles[i * 9 + 4] = vertices.get(vert[1] - 1)[1];
			verticesTriangles[i * 9 + 5] = vertices.get(vert[1] - 1)[2];
			texCoordsTriangles[i * 6 + 2] = texCoords.get(tex[1] - 1)[0];
			texCoordsTriangles[i * 6 + 3] = texCoords.get(tex[1] - 1)[1];
			normalsTriangles[i * 9 + 3] = normals.get(norm[1] - 1)[0];
			normalsTriangles[i * 9 + 4] = normals.get(norm[1] - 1)[1];
			normalsTriangles[i * 9 + 5] = normals.get(norm[1] - 1)[2];

			verticesTriangles[i * 9 + 6] = vertices.get(vert[2] - 1)[0];
			verticesTriangles[i * 9 + 7] = vertices.get(vert[2] - 1)[1];
			verticesTriangles[i * 9 + 8] = vertices.get(vert[2] - 1)[2];
			texCoordsTriangles[i * 6 + 4] = texCoords.get(tex[2] - 1)[0];
			texCoordsTriangles[i * 6 + 5] = texCoords.get(tex[2] - 1)[1];
			normalsTriangles[i * 9 + 6] = normals.get(norm[2] - 1)[0];
			normalsTriangles[i * 9 + 7] = normals.get(norm[2] - 1)[1];
			normalsTriangles[i * 9 + 8] = normals.get(norm[2] - 1)[2];
		}

		object.initVBO(verticesTriangles, 3, texCoordsTriangles, 2, normalsTriangles, 3);
	}

	private static void addVertex(float f1, float f2, float f3) {
		vertices.add(new float[] { f1, f2, f3 });
	}

	private static void addFaceVerts(int[] indexes) {
		if (indexes.length == 3) {
			faceVertsTriangles.add(new int[] { indexes[0], indexes[1], indexes[2] });
		}
	}

	private static void addTexCoord(float f1, float f2) {
		texCoords.add(new float[] { f1, f2 });
	}

	private static void addFaceTex(int[] indexes) {
		if (indexes.length == 3) {
			faceTexTriangles.add(new int[] { indexes[0], indexes[1], indexes[2] });
		}
	}

	private static void addNormal(float f1, float f2, float f3) {
		normals.add(new float[] { f1, f2, f3 });
	}

	private static void addFaceNormal(int[] indexes) {
		if (indexes.length == 3) {
			faceNormalsTriangles.add(new int[] { indexes[0], indexes[1], indexes[2] });
		}
	}
}
