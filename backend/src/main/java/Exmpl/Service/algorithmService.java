package Exmpl.Service;

import Exmpl.Entity.Algorithm;
import Exmpl.Mapper.algorithmMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class algorithmService {
    @Autowired
    algorithmMapper algMapper;
    public int insertalg (Algorithm alg){
        return algMapper.insert(alg);
    }
    public int updatealg (Algorithm alg){
        return algMapper.updateById(alg);
    }
    public int deletealg (Algorithm alg){
        return algMapper.deleteById(alg);
    }

    public Algorithm selectalgByid (Algorithm alg){
        return algMapper.selectById(alg.getAno());
    }
}
