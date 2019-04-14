package com.example.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import com.example.data.Constant;

import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.example.data.Constant.BYTES_PER_FLOAT;
import static javax.microedition.khronos.opengles.GL10.GL_FLOAT;

public class VertexArray {
    private final FloatBuffer floatBuffer;

    public VertexArray(float[] vertexData){
        floatBuffer = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    public void setVertexAttribPointer(int dataOffset,int attributeLocation,
                                       int componentCount,int stride){
        floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation,componentCount,GL_FLOAT,
                false,stride,floatBuffer);
        glEnableVertexAttribArray(attributeLocation);

        floatBuffer.position();
    }
}
