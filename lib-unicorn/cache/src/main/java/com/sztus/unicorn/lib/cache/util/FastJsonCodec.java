package com.sztus.unicorn.lib.cache.util;

public class FastJsonCodec { //extends BaseCodec {
//    private final Encoder encoder = in -> {
//        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
//        try {
//            ByteBufOutputStream os = new ByteBufOutputStream(out);
//            JSON.writeJSONString(os, in, SerializerFeature.WriteClassName);
//            return os.buffer();
//        } catch (IOException e) {
//            out.release();
//            throw e;
//        } catch (Exception e) {
//            out.release();
//            throw new IOException(e);
//        }
//    };
//    private final Decoder<Object> decoder = (buf, state) -> JSON.parseObject(new ByteBufInputStream(buf), Object.class);
//
//    @Override
//    public Decoder<Object> getValueDecoder() {
//        return decoder;
//    }
//
//    @Override
//    public Encoder getValueEncoder() {
//        return encoder;
//    }
}