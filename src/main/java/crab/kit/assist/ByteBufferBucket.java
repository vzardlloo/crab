package crab.kit.assist;


import crab.kit.AssertKit;

public class ByteBufferBucket {

    //FastByteBuffer的本体是个二维字符数组,默认是16个buffer
    private byte[][] buffers = new byte[16][];
    //buffer的数量
    private int buffersCount;
    //当前的buffer
    private byte[] currentBuffer;
    //当前的buffer在buffers中的下标
    private int currentBufferIndex = -1;
    //buffer的游标
    private int offset;
    //FastByteBuffer的总容量
    private int total;
    //每个新分配buffer最小大小
    private final int minChunkLen;


    public ByteBufferBucket() {
        this.minChunkLen = 1024;
    }

    public ByteBufferBucket(int capital) {
        AssertKit.isTrue(capital >= 0, "Invalid size: %d", new Object[]{capital});
        this.minChunkLen = capital;
    }

    /**
     * 新分配一个buffer,容量不够则扩容，并且bufferCount加一
     *
     * @param newSize
     */
    public void allocateNewBuffer(int newSize) {
        int delta = newSize - this.total;
        int newBufferSize = Math.max(this.minChunkLen, delta);
        ++this.currentBufferIndex;
        this.currentBuffer = new byte[newBufferSize];
        this.offset = 0;
        if (currentBufferIndex >= buffers.length) {
            byte[][] newBuffers = new byte[buffers.length << 1][];
            System.arraycopy(buffers, 0, newBuffers, 0, this.buffers.length);
            this.buffers = newBuffers;
        }
        this.buffers[currentBufferIndex] = this.currentBuffer;
        ++this.buffersCount;

    }

    public void clear() {
        this.currentBufferIndex = -1;
        this.currentBuffer = null;
        this.buffersCount = 0;
        this.total = 0;
        this.offset = 0;
    }

    public ByteBufferBucket append(byte[] array, int offset, int len) {
        int end = offset + len;
        if (offset >= 0 && len >= 0 && end <= array.length) {
            if (len == 0) {
                return this;
            } else {
                int newSize = this.total + len;
                int remaining = len;
                int part;
                if (this.currentBuffer != null) {
                    //比较先加入的buffer的长度和当前buffer的剩余长度
                    part = Math.min(len, this.currentBuffer.length - this.offset);
                    System.arraycopy(array, end - len, this.currentBuffer, this.offset, part);
                    remaining = len - part;
                    this.offset += part;
                    this.total += part;
                }
                //当前buffer装不下了
                if (remaining > 0) {
                    //分配新的buffer来装
                    this.allocateNewBuffer(newSize);
                    part = Math.min(remaining, this.currentBuffer.length - this.offset);
                    System.arraycopy(array, end - remaining, this.currentBuffer, this.offset, part);
                    this.offset += part;
                    this.total += part;
                }
                return this;
            }
        } else {
            throw new IndexOutOfBoundsException();
        }

    }

    public ByteBufferBucket append(byte[] buffer) {
        return append(buffer, 0, buffer.length);
    }

    public byte[] toArray() {
        int pos = 0;
        byte[] array = new byte[this.total];
        if (this.currentBufferIndex == -1) {
            return array;
        } else {
            //用++i 留下最后一个buffer
            for (int i = 0; i < this.currentBufferIndex; ++i) {
                int len = buffers[i].length;
                System.arraycopy(this.buffers[i], 0, array, pos, len);
                pos += len;
            }
            System.arraycopy(this.buffers[this.currentBufferIndex], 0, array, pos, this.offset);
            return array;
        }


    }


}
